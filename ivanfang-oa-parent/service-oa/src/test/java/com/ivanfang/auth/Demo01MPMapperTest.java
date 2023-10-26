package com.ivanfang.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ivanfang.auth.mapper.SysRoleMapper;
import com.ivanfang.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class Demo01MPMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void getAll() {          // get all records
        List<SysRole> list = sysRoleMapper.selectList(null);    // select without any condition
        System.out.println("list = " + list);
    }

    @Test                           // add a new record
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("Nova");
        sysRole.setRoleCode("staff");
        sysRole.setDescription("Nova is a staff");

        int rows = sysRoleMapper.insert(sysRole);   // rows: number of influenced rows

        System.out.println("rows = " + rows);
        System.out.println("sysRole = " + sysRole);
        System.out.println("sysRole.id = " + sysRole.getId());
    }

    @Test
    public void update() {          // update a record
        SysRole sysRole = sysRoleMapper.selectById(9);
        sysRole.setRoleName("Papaya");
        sysRole.setDescription(sysRole.getRoleName() + " is a " + sysRole.getRoleCode());
        int rows = sysRoleMapper.updateById(sysRole);
        System.out.println("rows = " + rows);
    }

    @Test
    public void delete() {          // delete a record
        int rows = sysRoleMapper.deleteById(9);
    }

    @Test
    public void batchDelete() {     // delete multiple records
        int rows = sysRoleMapper.deleteBatchIds(Arrays.asList(1, 2));
    }

    @Test
    public void queryWrapper01() {  // conditional query
        // 1. create query wrapper
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", "Papaya");     // options: eq, ge, gt, le, lt, like, ...

        // 2. query data based on query wrapper
        List<SysRole> list = sysRoleMapper.selectList(queryWrapper);
        System.out.println("list = " + list);
    }

    @Test
    public void queryWrapper02() {  // conditional query with lambda
        // 1. create lambda query wrapper
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName, "Papaya");

        // 2. query data based on query wrapper
        List<SysRole> list = sysRoleMapper.selectList(lambdaQueryWrapper);
        System.out.println("list = " + list);
    }

}
