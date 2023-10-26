package com.ivanfang.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ivanfang.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenuListByUserId(@Param("userId") Long userId);

}
