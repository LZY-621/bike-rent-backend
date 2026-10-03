package com.example.bikerent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bikerent.common.exception.BusinessException;
import com.example.bikerent.entity.User;
import com.example.bikerent.mapper.UserMapper;
import com.example.bikerent.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 用户 Service 实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public Page<User> pageUser(Long pageNum, Long pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getPhone, keyword)
            );
        }
        wrapper.orderByDesc(User::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public User recharge(Integer userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(400, "充值金额必须大于 0");
        }
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        BigDecimal newBalance = user.getBalance() == null
                ? amount
                : user.getBalance().add(amount);
        user.setBalance(newBalance);
        this.updateById(user);
        return user;
    }
}
