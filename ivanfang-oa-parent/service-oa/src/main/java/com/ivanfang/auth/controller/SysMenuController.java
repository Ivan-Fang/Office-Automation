package com.ivanfang.auth.controller;

import com.ivanfang.auth.service.SysMenuService;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.system.SysMenu;
import com.ivanfang.vo.system.AssignMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "選單管理介面")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("根據角色 id 查詢選單")
    @PreAuthorize("hasAuthority('btn.sysMenu.list')")
    @GetMapping("/get/menu/{roleId}")
    public Result getMenuByRoleId(@PathVariable Integer roleId) {
        List<SysMenu> menuList = sysMenuService.getMenuByRoleId(roleId);
        return Result.success(menuList);
    }

    @ApiOperation("為角色分配選單")
    @PreAuthorize("hasAuthority('btn.sysRole.assignAuth')")
    @PostMapping("/set/menu")
    public Result setMenu(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.setMenu(assignMenuVo);
        return Result.success();
    }

    @ApiOperation("新增選單")
    @PreAuthorize("hasAuthority('btn.sysMenu.add')")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.success();
    }

    @ApiOperation("刪除選單")
    @PreAuthorize("hasAuthority('btn.sysMenu.remove')")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Integer id) {
        return sysMenuService.removeMenuById(id);
    }

    @ApiOperation("修改選單")
    @PreAuthorize("hasAuthority('btn.sysMenu.update')")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.success();
    }

    @ApiOperation("以樹狀結構查詢所有選單")
    @PreAuthorize("hasAuthority('btn.sysMenu.list')")
    @GetMapping("/find/nodes")
    public Result findNodes() {
        List<SysMenu> sysMenuList = sysMenuService.findNodes();
        return Result.success(sysMenuList);
    }

}

