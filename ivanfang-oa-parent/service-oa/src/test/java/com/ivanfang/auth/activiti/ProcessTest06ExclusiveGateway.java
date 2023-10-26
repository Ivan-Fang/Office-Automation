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
import java.util.Map;

@SpringBootTest
public class ProcessTest06ExclusiveGateway {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/leave_exclusive_gateway.bpmn20.xml")
                .addClasspathResource("process/leave_exclusive_gateway.bpmn20.png")
                .name("請假流程_exclusive_gateway")
                .deploy();
    }

    @Test
    public void startProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 2);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave_exclusive_gateway", variables);
        System.out.println("process instance id = " + processInstance.getProcessInstanceId());  // a782de04-6e73-11ee-bb67-ca5ea92d054b
    }

    @Test
    public void checkAssignee() {
        // 先讓 Amy 完成第一個 task
        Task task1 = taskService.createTaskQuery()
                .processInstanceId("a782de04-6e73-11ee-bb67-ca5ea92d054b")
                .singleResult();
        taskService.complete(task1.getId());

        // 查詢目前 task 的 assignee 傳給誰了
        Task task2 = taskService.createTaskQuery()
                .processInstanceId("a782de04-6e73-11ee-bb67-ca5ea92d054b")
                .singleResult();
        System.out.println("assignee = " + task2.getAssignee());
    }

}
