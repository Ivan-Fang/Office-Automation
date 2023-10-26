package com.ivanfang.auth.activiti;

import com.ivanfang.ServiceAuthApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServiceAuthApplication.class)
public class ProcessTest02ActivateAndSuspend {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    // 激活或終止 process definition
    @Test
    public void activateOrSuspendProcessDefinition() {
        String processDefinitionId = "leave:3:691a0fe2-6cb5-11ee-8176-ca5ea92d054b";

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        // 如果某 process definition 是終止的，則激活之；反之，終止之
        boolean isSuspended = processDefinition.isSuspended();
        if (isSuspended) {
            // processDefinitionId, 是否激活其下的 process instance, 激活時間
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println(processDefinitionId + " 及其下的 process instance 被激活了");
        } else {
            // processDefinitionId, 是否終止其下的 process instance, 激活時間
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println(processDefinitionId + " 及其下的 process instance 被終止了");
        }
    }

    // 激活或終止 process instance
    @Test
    public void activateOrSuspendProcessInstance() {
        String processInstanceId = "f08fa817-6cde-11ee-a6ca-ca5ea92d054b";

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 如果某 process instance 是終止的，則激活之；反之，終止之
        boolean isSuspended = processInstance.isSuspended();
        if (isSuspended) {
            // processDefinitionId, 是否激活其下的 process instance, 激活時間
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println(processInstanceId + " 被激活了");
        } else {
            // processDefinitionId, 是否終止其下的 process instance, 激活時間
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println(processInstanceId + " 被終止了");
        }
    }

}
