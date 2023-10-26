package com.ivanfang.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.auth.service.SysRoleService;
import com.ivanfang.auth.service.SysUserRoleService;
import com.ivanfang.common.config.exception.MyException;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.model.system.SysUserRole;
import com.ivanfang.vo.system.SysRoleQueryVo;
import com.ivanfang.vo.system.AssginRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "角色管理介面")   // for api documentation, not necessary to add
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @ApiOperation("查詢所有角色")
    @PreAuthorize("hasAuthority('btn.sysRole.list')")
    @GetMapping("/get/all")
    public Result getAll() {     // get all SysRole in database
        List<SysRole> data = sysRoleService.list();
        return Result.success(data);
    }

    @ApiOperation("根據頁碼查詢角色")
    @PreAuthorize("hasAuthority('btn.sysRole.list')")
    @GetMapping("/get/{page}/{pageSize}")  // page：目前的頁碼、pageSize：每頁顯示的紀錄數、sysRoleQueryVo：條件物件
    public Result getByPage(@ApiParam("目前的頁碼") @PathVariable("page") Integer page,
                            @ApiParam("每頁顯示的紀錄數") @PathVariable("pageSize") Integer pageSize,
                            @ApiParam("條件物件") SysRoleQueryVo sysRoleQueryVo) {
        // 1. 創建 Page 物件
        Page<SysRole> sysRolePage = new Page<>(page, pageSize);

        // 2. 建立分頁查詢條件：判斷條件是否為空，不為空則進行封裝
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            queryWrapper.like("role_name", roleName);
        }

        // 3. 調用分頁查詢功能
        sysRoleService.page(sysRolePage, queryWrapper);
        return Result.success(sysRolePage);
    }

    @ApiOperation("根據 id 查詢角色")
    @PreAuthorize("hasAuthority('btn.sysRole.list')")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.getById(id);
        if (sysRole != null) {
            return Result.success(sysRole);
        } else {
            return Result.failed(null);
        }
    }

    @ApiOperation("新增角色")
    @PreAuthorize("hasAuthority('btn.sysRole.add')")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole sysRole) {
        boolean succeed = sysRoleService.save(sysRole);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('btn.sysRole.update')")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        boolean succeed = sysRoleService.updateById(sysRole);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("根據 id 刪除角色")
    @PreAuthorize("hasAuthority('btn.sysRole.remove')")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Integer id) {
        boolean succeed = sysRoleService.removeById(id);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量刪除角色")
    @PreAuthorize("hasAuthority('btn.sysRole.remove')")
    @DeleteMapping("/remove/batch")
    public Result removeBatch(@RequestBody List<Integer> idList) {
        boolean succeed = sysRoleService.removeByIds(idList);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("例外處理測試")
    @GetMapping("/exception")
    public void exceptionTest() {
        try {
            int i = 1 / 0;
        } catch (ArithmeticException e) {
            throw e;
        }
    }

    @ApiOperation("根據使用者 id 查詢角色")
    @PreAuthorize("hasAuthority('btn.sysRole.list')")
    @GetMapping("/get/roles/{userId}")
    public Result getRolesByUserId(@PathVariable Integer userId) {
        Map<String, Object> roleMap = sysRoleService.getRolesByUserId(userId);
        return Result.success(roleMap);
    }

    @ApiOperation("為使用者分配角色")
    @PreAuthorize("hasAuthority('btn.sysUser.assignRole')")
    @PutMapping("/set/roles")
    public Result setRoles(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.setRoles(assginRoleVo);
        return Result.success();
    }

}