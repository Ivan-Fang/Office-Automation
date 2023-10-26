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
public class ProcessTest07ParallelGateway {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/leave_parallel_gateway.bpmn20.xml")
                .addClasspathResource("process/leave_parallel_gateway.bpmn20.png")
                .name("請假流程_parallel_gateway")
                .deploy();
    }

    @Test
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave_parallel_gateway");
        System.out.println("process instance id = " + processInstance.getProcessInstanceId());  // cfe2ba2c-6e77-11ee-9ffd-ca5ea92d054b
    }

    @Test
    public void checkAssignee() {
        // 查詢樓層主管與經理是否同時收到任務
        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId("cfe2ba2c-6e77-11ee-9ffd-ca5ea92d054b")
                .list();

        // Amy, Glenn
        taskList.forEach(task -> {
            System.out.println("assignee = " + task.getAssignee());
        });
    }

}
