package com.ivanfang.auth.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProcessTest03UEL {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    // 部屬使用 uel value 建構的 process
    @Test
    public void deployProcessUelValue() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/overtime_uel_value.bpmn20.xml")
                .addClasspathResource("process/overtime_uel_value.bpmn20.png")
                .name("加班申請流程_UEL_value")
                .deploy();

        System.out.println("部屬 ID：" + deployment.getId());
        System.out.println("部屬名稱：" + deployment.getName());
    }

    // 啟動使用 uel value 建構的 process
    @Test
    public void startProcessInstanceUelValue() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("assignee1", "Amy");
        valueMap.put("assignee2", "Bob");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime_uel_value", valueMap);
        System.out.println("流程定義 ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程實例 ID：" + processInstance.getId());
    }

    // 查詢使用 uel value 建構的 assignee 的工作流
    @Test
    public void getTaskListUelValue() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("Amy")
                .list();
        taskList.forEach(task -> {
            System.out.println("流程實例 ID：" + task.getProcessInstanceId());
            System.out.println("任務 ID：" + task.getId());
            System.out.println("任務負責人：" + task.getAssignee());
            System.out.println("任務名稱：" + task.getName());
        });
    }

    // 部屬使用 uel method 建構的 process
    @Test
    public void deployProcessUelMethod() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/overtime_uel_method.bpmn20.xml")
                .addClasspathResource("process/overtime_uel_method.bpmn20.png")
                .name("加班申請流程_UEL_method")
                .deploy();

        System.out.println("部屬 ID：" + deployment.getId());
        System.out.println("部屬名稱：" + deployment.getName());
    }

    // 啟動使用 uel method 建構的 process
    @Test
    public void startProcessInstanceUelMethod() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime_uel_method");
        System.out.println("流程定義 ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程實例 ID：" + processInstance.getId());
    }

    // 查詢使用 uel method 建構的 assignee 的工作流
    @Test
    public void getTaskListUelMethod() {
        List<Task> taskList1 = taskService.createTaskQuery()
                .taskAssignee("Cheyenne")
                .list();
        taskList1.forEach(task -> {
            System.out.println("流程實例 ID：" + task.getProcessInstanceId());
            System.out.println("任務 ID：" + task.getId());
            System.out.println("任務負責人：" + task.getAssignee());
            System.out.println("任務名稱：" + task.getName());
        });
    }

}
