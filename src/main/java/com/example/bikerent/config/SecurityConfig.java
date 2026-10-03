package com.example.bikerent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置
 * - 注册 BCryptPasswordEncoder
 * - JWT 无状态认证
 * - 放行 /api/auth/**（登录/注册），其余 /api/** 需携带有效 token
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（前后端分离 API）
                .csrf(AbstractHttpConfigurer::disable)
                // 无状态会话（使用 JWT，不创建 session）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // 登录/注册放行
                        .requestMatchers("/api/dashboard/**").permitAll() // 仪表盘统计放行
                        .requestMatchers("/error").permitAll()
                        // Swagger / OpenAPI 放行
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()                  // 其余需认证
                )
                // 在用户名密码过滤器前加入 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
