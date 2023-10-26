package com.ivanfang.auth.activiti;

import com.ivanfang.ServiceAuthApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

// test 的 package 要跟 main application 的一樣，或是在 @SpringBootTest 裡自己指定 main application
@SpringBootTest(classes = ServiceAuthApplication.class)
public class ProcessTest01Basic {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 1. 從 classpath 下加載流程文件，並進行部屬 process
    @Test
    public void deployProcessByFile() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/leave.bpmn20.xml")
                .addClasspathResource("process/leave.png")
                .name("請假申請流程")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    // 1. 從 classpath 下加載流程壓縮檔，並進行部屬 process
    @Test
    public void deployProcessByZip() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("process/leave.bpmn20.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("請假申請流程 - by zip")
                .deploy();

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    // 2.1. 啟動 process instance
    @Test
    public void startProcessInstance() {
        // key = activiti explorer 中的 process identifier
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave");
        System.out.println("流程定義 ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程實例 ID：" + processInstance.getId());
        System.out.println("活動 ID：" + processInstance.getActivityId());
    }

    // 2.2. 啟動 process instance 並為之配置 business key，用於與業務程式碼進行關聯
    @Test
    public void startProcessInstanceWithBusinessKey() {
        // processDefinitionKey = "leave", businessKey = "1010"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", "1010");
        System.out.println("流程定義 ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程實例 ID：" + processInstance.getId());
        System.out.println("活動 ID：" + processInstance.getActivityId());
    }

    // 3. 查詢某 assignee 的所有任務
    @Test
    public void getTaskList() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("張三")
                .list();
        for (Task task : taskList) {
            System.out.println("流程實例 ID：" + task.getProcessInstanceId());
            System.out.println("任務 ID：" + task.getId());
            System.out.println("任務負責人：" + task.getAssignee());
            System.out.println("任務名稱：" + task.getName());
        }
    }

    // 4. 處理任務
    @Test
    public void completeTask() {
        // 查尋某 assignee 的目前的任務
        Task task = taskService.createTaskQuery()
                .taskAssignee("張三")
                .singleResult();
        taskService.complete(task.getId());
    }

    // 5. 查詢某 assignee 已完成的任務
    @Test
    public void getHistoricTask() {
        List<HistoricTaskInstance> historicTaskInstanceList1 = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("張三")
                .finished()
                .list();
        System.out.println("==== 張三 ====");
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList1) {
            System.out.println("流程實例 ID：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任務 ID：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任務負責人：" + historicTaskInstance.getAssignee());
            System.out.println("任務名稱：" + historicTaskInstance.getName());
        }

        List<HistoricTaskInstance> historicTaskInstanceList2 = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("李四")
                .finished()
                .list();
        System.out.println("==== 李四 ====");
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList2) {
            System.out.println("流程實例 ID：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任務 ID：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任務負責人：" + historicTaskInstance.getAssignee());
            System.out.println("任務名稱：" + historicTaskInstance.getName());
        }
    }

}
