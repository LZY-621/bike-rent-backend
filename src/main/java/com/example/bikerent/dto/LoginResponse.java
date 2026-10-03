package com.example.bikerent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer userId;
    private String username;
    private String nickname;
}
