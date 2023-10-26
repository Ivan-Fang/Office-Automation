package com.ivanfang.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;

public class JwtHelper {

    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;    // token 的有效時間
    private static String tokenSignKey = "19890604";                    // 用於生成 token 的 key

    // 根據 user id 與 username 生成 token
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    // 根據 token 回推 user id
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根據 token 回推 username
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 測試 JWT 的功能
    public static <Map> void main(String[] args) {
        java.util.Map<Long, String> userIdNameMap = new HashMap<>();
        userIdNameMap.put(1L, "admin");
        userIdNameMap.put(13L, "Amy");
        userIdNameMap.put(14L, "Glenn");
        userIdNameMap.put(16L, "Cheyenne");
        userIdNameMap.put(17L, "Mateo");

        for (java.util.Map.Entry<Long, String> entry : userIdNameMap.entrySet()) {
            String token = JwtHelper.createToken(entry.getKey(), entry.getValue());
            System.out.println("userId = " + JwtHelper.getUserId(token) + ", username = " + JwtHelper.getUsername(token));
            System.out.println(token + "\n");
        }
    }

}
