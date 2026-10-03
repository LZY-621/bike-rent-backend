package com.example.bikerent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bikerent.entity.User;

import java.math.BigDecimal;

/**
 * 用户 Service 接口
 */
public interface UserService extends IService<User> {

    /** 根据用户名查询 */
    User getByUsername(String username);

    /** 分页查询用户（支持关键字：用户名/昵称/手机号） */
    Page<User> pageUser(Long pageNum, Long pageSize, String keyword);

    /**
     * 账户充值
     * @param userId 用户ID
     * @param amount 充值金额（必须 > 0）
     * @return 更新后的用户
     */
    User recharge(Integer userId, BigDecimal amount);
}
