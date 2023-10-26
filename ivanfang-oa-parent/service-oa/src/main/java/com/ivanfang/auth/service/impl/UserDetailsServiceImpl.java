package com.ivanfang.auth.service.impl;

import com.ivanfang.auth.service.SysMenuService;
import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.model.system.SysMenu;
import com.ivanfang.model.system.SysUser;
import com.ivanfang.security.custom.CustomUser;
import com.ivanfang.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根據 username 從資料庫中查出 SysUser，並封裝成 UserDetails
        SysUser sysUser = sysUserService.getByUsername(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException("使用者名稱不存在");
        }

        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("該帳號已被停用");
        }

        // 透過 username 查詢（按鈕）權限
        List<String> permList = sysMenuService.getPermListByUserId(sysUser.getId());

        // 將選單字串轉成 Authority 格式
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String perm : permList) {
            authList.add(new SimpleGrantedAuthority(perm));
        }

        // 將 sysUser 與 authList 封裝成 UserDetails，返回給 TokenLoginFilter
        return new CustomUser(sysUser, authList);
    }

}
