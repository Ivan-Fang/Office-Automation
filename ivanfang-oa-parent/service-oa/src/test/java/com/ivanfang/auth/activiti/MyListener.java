package com.ivanfang.auth.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("樓層主管審批")) {
            delegateTask.setAssignee("Emma");
        } else if (delegateTask.getName().equals("經理審批")) {
            delegateTask.setAssignee("Frank");
        }
    }
}
