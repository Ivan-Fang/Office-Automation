package com.ivanfang.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.model.process.Process;
import com.ivanfang.model.process.ProcessRecord;
import com.ivanfang.model.process.ProcessTemplate;
import com.ivanfang.model.system.SysUser;
import com.ivanfang.process.mapper.ProcessMapper;
import com.ivanfang.process.service.ProcessRecordService;
import com.ivanfang.process.service.ProcessService;
import com.ivanfang.process.service.ProcessTemplateService;
import com.ivanfang.process.service.ProcessTypeService;
import com.ivanfang.security.custom.LoginUserInfoHelper;
import com.ivanfang.vo.process.ApprovalVo;
import com.ivanfang.vo.process.ProcessFormVo;
import com.ivanfang.vo.process.ProcessQueryVo;
import com.ivanfang.vo.process.ProcessVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessRecordService processRecordService;

    @Autowired
    private HistoryService historyService;

    @Override
    public Page<ProcessVo> selectPage(Page<Process> pageParam, ProcessQueryVo processQueryVo) {
        return baseMapper.selectPage(pageParam, processQueryVo);
    }

    @Override
    public void deployByZip(String filepath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filepath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程 " + deployment.getName() + " (" + deployment.getId() + ") 部署完成");
    }

    // 啟動流程實例並將記錄存到 process 表中
    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 1. 獲取使用者資訊
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        // 2. 根據 processFormVo 獲取 ProcessTemplate
        ProcessTemplate processTemplate =
                processTemplateService.getById(processFormVo.getProcessTemplateId());

        // 3. 將流程資訊存到 process 表中
        Process process = new Process();

        BeanUtils.copyProperties(processFormVo, process);

        process.setProcessCode(System.currentTimeMillis() + "");
        process.setUserId(sysUser.getId());
        process.setStatus(1);   // 審批中
        process.setTitle("[" + sysUser.getUsername() + "] 的 [" + processTemplate.getName() + "] 申請");

        // 這邊先插入 process 到表中，以獲取 process id，當作 business key
        baseMapper.insert(process);

        // 4. 準備啟動參數、啟動流程實例
        // 4.1. process definition key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();

        // 4.2. business key
        String businessKey = process.getId() + "";

        // 4.3. process variables
        JSONObject formValues = JSON.parseObject(processFormVo.getFormValues());
        JSONObject formShowData = formValues.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formShowData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", map);

        // 4.4. 啟動流程實例
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        // 5. 儲存流程實例相關資訊到 process 表中
        // 5.1. 下一個審批人（可能有多個，因為會有 parallel gateway 或 inclusive gateway）
        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .list();
        List<String> assigneeList = new ArrayList<>();
        taskList.forEach(t -> {
            assigneeList.add(t.getAssignee());
        });
        // TODO 推送訊息

        // 5.2. 流程實例 ID
        process.setProcessInstanceId(processInstance.getProcessInstanceId());

        // 5.3. 描述
        process.setDescription("等待 [" + StringUtils.join(assigneeList.toArray(), ",") + "] 進行審批");

        // 6. 更新 process 到表中
        baseMapper.updateById(process);

        // 7. 做紀錄
        processRecordService.record(process.getId(), "發起申請", process.getStatus());
    }

    // 查詢目前使用者的待辦任務
    @Override
    public Page<ProcessVo> getPending(Page<Process> pageParam) {
        // 1. 查詢目前使用者的待辦任務
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        // 2. 只取目前分頁的資料
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<Task> taskList = query.listPage(begin, size);
        long totalCount = query.count();    // 共有多少筆記錄

        // 3. 將 Task 轉成 ProcessVo
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : taskList) {
            // 根據 Task 獲取 ProcessInstance
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            // 根據 ProcessInstance 獲取 process id，進而獲取 Process
            // 由於之前在 startUp() 中將 business key 設為 process id，因此直接從 business key 中獲取即可
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Long processId = Long.parseLong(businessKey);
            Process process = baseMapper.selectById(processId);

            // 將 Process 複製到 ProcessVo 中，並手動設置其它值
            if (process != null) {
                ProcessVo processVo = new ProcessVo();
                BeanUtils.copyProperties(process, processVo);
                processVo.setTaskId(task.getId());
                processVoList.add(processVo);
            }
        }

        // 4. 返回結果
        Page<ProcessVo> page = new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processVoList);
        return page;
    }

    // 根據 process id 顯示審批詳情資訊
    // 包含：process、processRecordList、processTemplate、canApprove
    @Override
    public Map<String, Object> show(Integer processId) {
        // 1. process
        Process process = baseMapper.selectById(processId);

        // 2. processRecordList
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, processId);
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        // 3. processTemplate
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        /* 4. canApprove
         * 判斷目前的使用者是否可以審批此流程
         * 可以看到流程不一定能對此進行審批，此外，不能重複審批
         */
        boolean canApprove = false;
        String username = LoginUserInfoHelper.getUsername();
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : taskList) {
            if (task.getAssignee().equals(username)) {
                canApprove = true;
                break;
            }
        }

        // 5. 將查詢到的資訊封裝到 map 裡並回傳
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("canApprove", canApprove);
        return map;
    }

    // 執行任務審批
    @Override
    public void approve(ApprovalVo approvalVo) {
        // 1. 完成任務
        String taskId = approvalVo.getTaskId();
        if (approvalVo.getStatus() == 1) {  // 審批通過
            Map<String, Object> variables = taskService.getVariables(taskId);
            taskService.complete(approvalVo.getTaskId(), variables);
        } else {                            // 審批駁回
            this.endTask(taskId);   // 直接結束整個流程
        }

        // 2. 更新 process record 與 process 表
        String recordDescription = "";
        if (approvalVo.getStatus() == 1) {
            recordDescription = "核准申請";
        } else {
            recordDescription = "駁回申請";
        }
        processRecordService.record(approvalVo.getProcessId(), recordDescription, approvalVo.getStatus());

        // 3. 更新 process
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for (Task task : taskList) {
                assigneeList.add(task.getAssignee());
            }
            process.setStatus(1);
            process.setDescription("等待 [" + StringUtils.join(assigneeList.toArray(), ",") + "] 進行審批");
        } else {        // 表示沒有下一個審批人了，流程結束
            if (approvalVo.getStatus() == 1) {
                process.setStatus(2);
                process.setDescription("審批完成（核准）");
            } else {
                process.setStatus(-1);
                process.setDescription("審批完成（駁回）");
            }
        }
        baseMapper.updateById(process);

    }

    // 查詢目前使用者已處理的任務（分頁查詢）
    @Override
    public Page<ProcessVo> getProcessed(Page<Process> pageParam) {
        // 查詢已處理的任務
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished()
                .orderByTaskCreateTime()
                .desc();

        // 只封裝目前分頁的資料
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int pageSize = (int) (pageParam.getSize());     // 每頁顯示的任務數
        List<HistoricTaskInstance> historicTaskInstanceList = query.listPage(begin, pageSize);
        int totalCount = (int) query.count();           // 已完成的總任務數

        // 獲取 Process 並轉成 ProcessVo
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            // 獲取 Process
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId, processInstanceId);
            Process process = baseMapper.selectOne(wrapper);

            if (process != null) {
                // 轉成 ProcessVo
                ProcessVo processVo = new ProcessVo();
                System.out.println("process = " + process);
                System.out.println("processVo = " + processVo);
                BeanUtils.copyProperties(process, processVo);
                processVoList.add(processVo);
            }
        }

        // 回傳分頁資料
        Page<ProcessVo> pageModel = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    // 查詢目前使用者已發起的任務（分頁查詢）
    @Override
    public Page<ProcessVo> getStarted(Page<Process> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        return baseMapper.selectPage(pageParam, processQueryVo);
    }

    private void endTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)) {
            return;
        }

        FlowNode endFlowNode = endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        List originalSequenceFlowList = new ArrayList();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());

        currentFlowNode.getOutgoingFlows().clear();

        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId("newSequenceFlow");
        sequenceFlow.setSourceFlowElement(currentFlowNode);
        sequenceFlow.setTargetFlowElement(endFlowNode);

        List newSequenceFlowList = new ArrayList();
        newSequenceFlowList.add(sequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        taskService.complete(taskId);
    }

    public List<Task> getCurrentTaskList(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

}
