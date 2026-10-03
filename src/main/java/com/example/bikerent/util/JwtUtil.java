package com.example.bikerent.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {

    /** 密钥（至少 256 位） */
    private static final String SECRET = "bike-rent-secret-key-for-jwt-signing-2026-very-long";

    /** 有效期：24 小时 */
    private static final long EXPIRATION = 24 * 60 * 60 * 1000L;

    private final SecretKey key;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT token
     */
    public String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * 解析 JWT token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 token 中取出用户ID
     */
    public Integer getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Integer.class);
    }

    /**
     * 从 token 中取出用户名
     */
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 验证 token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
