package com.ivanfang.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivanfang.auth.mapper.SysRoleMapper;
import com.ivanfang.auth.service.SysRoleService;
import com.ivanfang.auth.service.SysUserRoleService;
import com.ivanfang.model.system.SysRole;
import com.ivanfang.model.system.SysUserRole;
import com.ivanfang.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> getRolesByUserId(Integer userId) {   // 查詢使用者被分配到的角色、查詢所有角色
        // 1. 查詢所有可用的角色
        List<SysRole> allRoleList = baseMapper.selectList(null);

        // 2. 查詢該使用者邏輯上被分配到的角色 id（但該角色 id 不一定有對應的角色）
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> logicUserRoleList = sysUserRoleService.list(wrapper);
        List<Long> logicRoleIdList = new ArrayList<>();
        logicUserRoleList.forEach(userRole -> {
            logicRoleIdList.add(userRole.getRoleId());
        });

        // 3. 有出現在 allRoleList 中的角色 id 才會被回傳到前端
        List<SysRole> realRoleList = new ArrayList<>();
        allRoleList.forEach(role -> {
            if (logicRoleIdList.contains(role.getId())) {
                realRoleList.add(role);
            }
        });

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", realRoleList);    // 這邊的 assign 拼錯是為了與前端的名稱保持一致
        roleMap.put("allRolesList", allRoleList);       // 因為老師的教材中前端裡的 assign 拼錯了
                                                        // 而不保持一致的話會有 bug
        return roleMap;
    }

    @Override
    public void setRoles(AssginRoleVo assginRoleVo) {               // 為使用者分配角色
        // 1. 刪除原本分配到的角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        // 2. 更新使用者的角色
        for (Long roleId : assginRoleVo.getRoleIdList()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
