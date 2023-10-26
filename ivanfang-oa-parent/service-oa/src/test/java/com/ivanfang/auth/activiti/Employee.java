package com.ivanfang.auth.activiti;

import org.springframework.stereotype.Component;

@Component
public class Employee {

    String employeeName;

    public String getEmployeeName(Integer id) {
        if (id == 1) {
            return "Cheyenne";
        } else if (id == 2) {
            return "Dina";
        } else {
            return "Admin";
        }
    }

}
