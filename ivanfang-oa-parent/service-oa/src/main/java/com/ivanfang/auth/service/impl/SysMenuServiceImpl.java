package com.ivanfang.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ivanfang.auth.mapper.SysMenuMapper;
import com.ivanfang.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivanfang.auth.service.SysRoleMenuService;
import com.ivanfang.auth.utils.MenuHelper;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.system.SysMenu;
import com.ivanfang.model.system.SysRoleMenu;
import com.ivanfang.vo.system.AssignMenuVo;
import com.ivanfang.vo.system.MetaVo;
import com.ivanfang.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    // 以樹狀結構查詢所有選單
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    // 檢查該 menu 下是否有子 menu，若有則無法刪除
    @Override
    public Result removeMenuById(Integer id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer childNum = baseMapper.selectCount(wrapper);

        if (childNum > 0) {
            return Result.failed();
        }
        baseMapper.deleteById(id);
        return Result.success();
    }

    // 根據角色 id 查詢選單
    @Override
    public List<SysMenu> getMenuByRoleId(Integer roleId) {
        // 1. 查詢所有可用的選單
        LambdaQueryWrapper<SysMenu> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> allMenuList = baseMapper.selectList(menuWrapper);

        // 2. 查詢該角色邏輯上被分配到的選單
        LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> logicRoleMenuList = sysRoleMenuService.list(roleMenuWrapper);
        List<Long> logicMenuIdList = new ArrayList<>();
        logicRoleMenuList.forEach(roleMenu -> {
            logicMenuIdList.add(roleMenu.getMenuId());
        });

        // 3. 若邏輯選單有出現在所有可用的選單中，則將其 selected 設為 true
        allMenuList.forEach(menu -> {
            if (logicMenuIdList.contains(menu.getId())) {
                menu.setSelect(true);
            } else {
                menu.setSelect(false);
            }
        });

        // 4. 將選單列表轉成樹狀結構返回
        List<SysMenu> resultMenuList = MenuHelper.buildTree(allMenuList);

        return resultMenuList;
    }

    // 為角色分配選單
    @Override
    public void setMenu(AssignMenuVo assignMenuVo) {
        // 1. 刪除該角色原有的選單
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);

        // 2. 重新為該角色分配選單
        for (Long menuId : assignMenuVo.getMenuIdList()) {
            if (menuId != null) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenuService.save(sysRoleMenu);
            }
        }
    }

    @Override
    public List<RouterVo> getRouterListByUserId(Long userId) {
        // 1. 若為管理員（userId = 1）則直接給予所有權限，反之查詢該使用者的權限
        List<SysMenu> menuList = null;
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            menuList = baseMapper.selectList(wrapper);
        } else {
            menuList = baseMapper.getMenuListByUserId(userId);
        }
        
        // 2. 將 menu list 轉成樹狀結構
        List<SysMenu> menuTreeList = MenuHelper.buildTree(menuList);
        
        // 3. 將 menu tree list 轉成 router 的格式
        List<RouterVo> routerList = this.buildRouter(menuTreeList);
        
        return  routerList;
    }

    // 將 menu 轉成 router 的格式
    private List<RouterVo> buildRouter(List<SysMenu> menuList) {
        List<RouterVo> routerList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            // 將 menu 轉成 router 的格式
            RouterVo routerVo = new RouterVo();
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setPath(getRouterPath(menu));
            routerVo.setComponent(menu.getComponent());
            routerVo.setMeta(new MetaVo(menu.getName(), menu.getIcon()));

            // type -> 0：系統管理、1：一般選單、2：隱藏選單/按鈕
            List<SysMenu> childMenuList = menu.getChildren();
            if (menu.getType() == 1) {
                // 設置隱藏選單（type 為 2 且 component 不為空）
                for (SysMenu childMenu : childMenuList) {
                    if (!StringUtils.isEmpty(childMenu.getComponent())) {
                        RouterVo hiddenRouterVo = new RouterVo();
                        hiddenRouterVo.setHidden(true);
                        hiddenRouterVo.setAlwaysShow(false);
                        hiddenRouterVo.setPath(getRouterPath(childMenu));
                        hiddenRouterVo.setComponent(childMenu.getComponent());
                        hiddenRouterVo.setMeta(new MetaVo(childMenu.getName(), childMenu.getIcon()));
                        routerList.add(hiddenRouterVo);
                    }
                }
            } else {    // 如果 childMenuList 不為空，則利用遞迴設置 chile menu
                if (!CollectionUtils.isEmpty(childMenuList)) {
                    routerVo.setAlwaysShow(true);
                    routerVo.setChildren(buildRouter(childMenuList));
                }
            }
            routerList.add(routerVo);
        }
        return routerList;
    }

    private String getRouterPath(SysMenu menu) {
        // 除了最上層的 router 以外，其它的路徑都不用加 "/"
        String routerPath;
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        } else {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> getPermListByUserId(Long userId) {
        // 1. 若為管理員（userId = 1）則直接給予所有權限，反之查詢該使用者的權限
        List<SysMenu> menuList = null;
        if (userId == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            menuList = baseMapper.selectList(wrapper);
        } else {
            menuList = baseMapper.getMenuListByUserId(userId);
        }

        // 2. 從 menuList 中抽取出按鈕的部分（type = 2）
        List<String> permList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getType() == 2) {
                permList.add(menu.getPerms());
            }
        }

        return permList;
    }

}
