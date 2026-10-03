package com.example.bikerent.config;

import com.example.bikerent.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 从请求头 Authorization: Bearer <token> 中解析 token，
 * 验证通过后将用户信息写入 SecurityContext，供后续业务使用。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length());
            try {
                if (jwtUtil.validateToken(token)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Integer userId = jwtUtil.getUserId(token);
                    String username = jwtUtil.getUsername(token);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 把 userId 放到 details 里，业务层可通过
                    // ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
                    // 获取，这里直接放进 principal 更简单
                    auth.setDetails(userId);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // token 无效，清空上下文
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
