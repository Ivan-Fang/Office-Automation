package com.ivanfang.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.system.SysMenu;
import com.ivanfang.vo.system.AssignMenuVo;
import com.ivanfang.vo.system.RouterVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    Result removeMenuById(Integer id);

    List<SysMenu> getMenuByRoleId(Integer roleId);

    void setMenu(AssignMenuVo assignMenuVo);

    List<RouterVo> getRouterListByUserId(Long userId);

    List<String> getPermListByUserId(Long userId);
}
