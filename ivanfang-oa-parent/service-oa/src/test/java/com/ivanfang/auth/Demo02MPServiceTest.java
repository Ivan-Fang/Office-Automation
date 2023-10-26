package com.ivanfang.auth;

import com.ivanfang.auth.service.SysRoleService;
import com.ivanfang.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Demo02MPServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void getAll() {          // get all records
        List<SysRole> list = sysRoleService.list();
        System.out.println(list);
    }

}
