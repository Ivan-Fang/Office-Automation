package com.ivanfang.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.model.system.SysUser;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysRole> getRoleListById(@Param("userId") Long userId);
}
