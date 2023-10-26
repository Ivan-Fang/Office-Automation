package com.ivanfang.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> getRolesByUserId(Integer userId);
    void setRoles(AssginRoleVo assginRoleVo);
}
