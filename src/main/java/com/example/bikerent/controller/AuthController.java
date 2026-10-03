package com.example.bikerent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.bikerent.common.Result;
import com.example.bikerent.common.exception.BusinessException;
import com.example.bikerent.dto.LoginRequest;
import com.example.bikerent.dto.LoginResponse;
import com.example.bikerent.dto.RegisterRequest;
import com.example.bikerent.entity.User;
import com.example.bikerent.service.UserService;
import com.example.bikerent.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 认证控制器 — 用户登录/注册
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. 参数校验
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BusinessException(400, "密码不能为空");
        }

        // 2. 查用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 3. 验密码 —— 兼容旧明文密码，登录成功后自动升级为 BCrypt
        String storedPwd = user.getPassword();
        boolean pwdOk;
        if (storedPwd != null && storedPwd.startsWith("$2")) {
            // BCrypt 加密的密码
            pwdOk = passwordEncoder.matches(request.getPassword(), storedPwd);
        } else {
            // 旧版明文密码
            pwdOk = storedPwd != null && storedPwd.equals(request.getPassword());
            if (pwdOk) {
                // 静默升级为 BCrypt
                User upgrade = new User();
                upgrade.setId(user.getId());
                upgrade.setPassword(passwordEncoder.encode(request.getPassword()));
                userService.updateById(upgrade);
            }
        }
        if (!pwdOk) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 4. 生成 JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        LoginResponse resp = new LoginResponse(token, user.getId(), user.getUsername(), user.getNickname());
        return Result.success("登录成功", resp);
    }

    /**
     * 用户注册
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        // 1. 参数校验
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BusinessException(400, "密码至少 6 位");
        }

        // 2. 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userService.count(wrapper) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 3. 保存用户（密码 BCrypt 加密）
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setBalance(BigDecimal.ZERO);

        boolean saved = userService.save(user);
        if (!saved) {
            throw new BusinessException("注册失败");
        }

        // 密码置空不返回
        user.setPassword(null);
        return Result.success("注册成功", user);
    }
}
