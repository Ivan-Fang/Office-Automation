package com.ivanfang.auth.activiti;


import lombok.Getter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessTest05CandidateUser {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployAndStartProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/overtime_candidate_user.bpmn20.xml")
                .addClasspathResource("process/overtime_candidate_user.bpmn20.png")
                .name("加班申請流程_candidate_user")
                .deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime_candidate_user");
        System.out.println("process instance id = " + processInstance.getId());
    }

    // 獲取某候選人的任務清單
    @Test
    public void getCandidateUserTask() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser("Amy")    // Note：這裡的使用者名稱要有出現在資料庫（sys_user 表）裡
                .list();
        taskList.forEach(task -> {
            System.out.println("任務 ID：" + task.getId());
            System.out.println("任務負責人：" + task.getAssignee());  // null，因為尚未指定哪個候選人是負責人
        });
    }

    // 拾取任務
    @Test
    public void claimTask() {
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("Amy")
                .singleResult();
        if (task != null) {
            taskService.claim(task.getId(), "Amy");
            System.out.println("任務拾取成功");
        }
    }

    // 歸還任務
    @Test
    public void taskReturn() {
        String taskId = "f0943bfb-6cde-11ee-a6ca-ca5ea92d054b";
        taskService.setAssignee(taskId, null);
    }

    // 任務交接
    @Test
    public void taskTransition() {
        String taskId = "f0943bfb-6cde-11ee-a6ca-ca5ea92d054b";
        taskService.setAssignee(taskId, "Marry");
    }

}
