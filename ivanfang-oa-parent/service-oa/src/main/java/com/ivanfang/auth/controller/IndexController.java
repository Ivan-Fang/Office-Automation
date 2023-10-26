package com.ivanfang.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ivanfang.auth.service.SysMenuService;
import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.common.config.exception.MyException;
import com.ivanfang.common.jwt.JwtHelper;
import com.ivanfang.common.result.Result;
import com.ivanfang.common.utils.MD5;
import com.ivanfang.model.system.SysUser;
import com.ivanfang.vo.system.LoginVo;
import com.ivanfang.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "後台登錄管理介面")
@RestController
@RequestMapping("/admin/system/index")
@CrossOrigin
public class IndexController {

    /*
        模仿 vue-admin-template 中的 mock 創建後端的介面
     */

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
//        // 預設寫法（未根據 username、password 進行登入） {"code":200, "data":"admin-token"}
//        Map<String , Object> data = new HashMap<>();
//        data.put("token", "admin-token")

        // 1. 讀取使用者名稱與密碼
        String username = loginVo.getUsername();
        String passwordInput = MD5.encrypt(loginVo.getPassword());

        // 2. 根據使用者名稱查詢資料庫
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        // 3.1. 確認使用者名稱存在
        if (sysUser == null) {
            throw new MyException(201, "使用者（名稱）不存在");
        }

        // 3.2. 確認密碼正確
        String passwordDB = sysUser.getPassword();
        if (!passwordDB.equals(passwordInput)) {
            throw  new MyException(201, "密碼錯誤");
        }

        // 3.3. 確認使用者未被禁用（0：禁用、1：可用）
        if (sysUser.getStatus().intValue() == 0) {
            throw new MyException(201, "使用者已被禁用");
        }

        // 4. 傳送 token 給前端，並儲存到 cookie 中
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        Map<String , Object> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        // 1. 從 request header 獲取 token
        String token = request.getHeader("token");

        // 2. 根據 token 獲取 user id
        Long userId = JwtHelper.getUserId(token);

        // 3. 根據 user id 查詢使用者的資訊
        SysUser user = sysUserService.getById(userId);

        // 4. 根據 user id 查詢該使用者可以使用的選單（router）
        List<RouterVo> routerList = sysMenuService.getRouterListByUserId(userId);

        // 5. 根據 user id 查詢該使用者可以使用的按鈕（perms, permission）
        List<String> permList = sysMenuService.getPermListByUserId(userId);

        // 6. 將相關資訊回傳到前端
        Map<String, Object> data = new HashMap<>();
        data.put("roles", "[admin]");
        data.put("name", user.getUsername());
        data.put("avatar", StringUtils.isEmpty(user.getHeadUrl()) ? "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif" : user.getHeadUrl());
        data.put("routers", routerList);
        data.put("buttons", permList);

        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

}
