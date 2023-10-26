package com.ivanfang.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.common.result.Result;
import com.ivanfang.common.utils.MD5;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.model.system.SysUser;
import com.ivanfang.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "使用者管理介面")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("根據頁碼查詢使用者")
    @ResponseBody
    @PreAuthorize("hasAuthority('btn.sysUser.list')")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getByPage(@PathVariable Integer page,
                            @PathVariable Integer pageSize,
                            SysUserQueryVo sysUserQueryVo) {
        // 獲取使用者的基本資訊
        Page<SysUser> sysUserPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword();
        String beginTime = sysUserQueryVo.getCreateTimeBegin();
        String endTime = sysUserQueryVo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(username)) {
            lambdaQueryWrapper.like(SysUser::getUsername, username);
        }
        if (!StringUtils.isEmpty(beginTime)) {
            lambdaQueryWrapper.ge(SysUser::getCreateTime, beginTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            lambdaQueryWrapper.like(SysUser::getCreateTime, endTime);
        }

        sysUserService.page(sysUserPage, lambdaQueryWrapper);

        List<SysUser> userList = sysUserPage.getRecords();

        // 獲取每個 SysUser 的 roleList
        for (SysUser user : userList) {
            List<SysRole> roleList = sysUserService.getRoleListById(user.getId());
            user.setRoleList(roleList);
        }

        sysUserPage.setRecords(userList);
        return Result.success(sysUserPage);
    }

    @ApiOperation("根據 id 查詢使用者")
    @PreAuthorize("hasAuthority('btn.sysUser.list')")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Integer id) {
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser != null) {
            return Result.success(sysUser);
        } else {
            return Result.failed(null);
        }
    }

    @ApiOperation("查詢所有使用者")
    @PreAuthorize("hasAuthority('btn.sysUser.list')")
    @GetMapping("/get/all")
    public Result getAll() {
        List<SysUser> data = sysUserService.list(null);
        return Result.success(data);
    }

    @ApiOperation("新增使用者")
    @PreAuthorize("hasAuthority('btn.sysUser.add')")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser) {
        // 使用 MD5 加密使用者的密碼（MD5 是一個不可逆的加密演算法，只能加密不能解密）
        String passwordMD5 = MD5.encrypt(sysUser.getPassword());
        sysUser.setPassword(passwordMD5);

        boolean succeed = sysUserService.save(sysUser);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("修改使用者資訊")
    @PreAuthorize("hasAuthority('btn.sysUser.update')")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser sysUser) {
        boolean succeed = sysUserService.updateById(sysUser);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("根據 id 刪除使用者")
    @PreAuthorize("hasAuthority('btn.sysUser.remove')")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Integer id) {
        boolean succeed = sysUserService.removeById(id);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量刪除使用者")
    @PreAuthorize("hasAuthority('btn.sysUser.remove')")
    @DeleteMapping("/remove/batch")
    public Result removeBatch(@RequestBody List<Integer> idList) {
        boolean succeed = sysUserService.removeByIds(idList);
        if (succeed) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("更新使用者的狀態")
    @PreAuthorize("hasAuthority('btn.sysUser.update')")
    @PutMapping("/update/status/{id}/{status}")
    public Result updateStatus(@PathVariable Integer id,
                               @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.success();
    }

}

