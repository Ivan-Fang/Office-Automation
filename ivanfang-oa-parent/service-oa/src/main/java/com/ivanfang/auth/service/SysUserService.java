package com.ivanfang.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.model.system.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    void updateStatus(Integer id, Integer status);

    SysUser getByUsername(String username);

    List<SysRole> getRoleListById(Long userId);
}
