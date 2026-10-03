package com.example.bikerent.dto;

import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String nickname;
    private String phone;
}
