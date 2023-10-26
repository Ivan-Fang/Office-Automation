package com.ivanfang.security.filter;

import com.alibaba.fastjson2.JSON;
import com.ivanfang.common.jwt.JwtHelper;
import com.ivanfang.common.result.ResponseUtil;
import com.ivanfang.common.result.Result;
import com.ivanfang.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * 當使用者透過輸入網址等方法造訪網頁時，
 * 利用此 filter 認證該使用者是否已經登入
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 如果使用者造訪的是登入頁面，則直接放行
        if ("/admin/system/index/login".equals(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(httpServletRequest ,httpServletResponse);
            return;
        }

        // 反之，確認使用者是否已登入
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthentication(httpServletRequest);
        if (null != usernamePasswordAuthenticationToken) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            ResponseUtil.out(httpServletResponse, Result.loginFailed());
        }
    }

    // 從 request header 中取出 username 與 authorization list 並進行授權驗證
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                // 將 username 與 userId 存到 ThreadLocal 中
                LoginUserInfoHelper.setUsername(username);
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));

                // 根據 username 從 redis 中取出 authorization list
                String authJsonString = (String) redisTemplate.opsForValue().get(username);
                List<Map> mapList = JSON.parseArray(authJsonString, Map.class);
                List<SimpleGrantedAuthority> authList = new ArrayList<>();
                for (Map map : mapList) {
                    authList.add(new SimpleGrantedAuthority((String) map.get("authority")));
                }

                return new UsernamePasswordAuthenticationToken(username, null , authList);
            } else {
                return new UsernamePasswordAuthenticationToken(username, null , Collections.emptyList());
            }
        }
        return null;
    }

}
