package com.ivanfang.auth.utils;

import com.ivanfang.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> tree = new ArrayList<>();

        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().intValue() == 0) {   // parentId 為 0 的即為最上層的選單
                tree.add(getChildren(sysMenu, sysMenuList));
            }
        }

        return tree;
    }

    public static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        for (SysMenu s : sysMenuList) {
            if (s.getParentId().intValue() == sysMenu.getId().intValue()) {
                sysMenu.getChildren().add(getChildren(s, sysMenuList));
            }
        }

        return sysMenu;
    }

}
