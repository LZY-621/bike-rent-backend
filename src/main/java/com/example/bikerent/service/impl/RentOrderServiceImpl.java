package com.example.bikerent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bikerent.common.exception.BusinessException;
import com.example.bikerent.entity.Bike;
import com.example.bikerent.entity.RentOrder;
import com.example.bikerent.entity.User;
import com.example.bikerent.mapper.RentOrderMapper;
import com.example.bikerent.service.BikeService;
import com.example.bikerent.service.RentOrderService;
import com.example.bikerent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 租赁订单 Service 实现类
 */
@Service
public class RentOrderServiceImpl extends ServiceImpl<RentOrderMapper, RentOrder> implements RentOrderService {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private UserService userService;

    @Override
    public Page<RentOrder> pageRentOrder(Long pageNum, Long pageSize, Integer userId, Integer bikeId, Integer status) {
        Page<RentOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RentOrder> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(RentOrder::getUserId, userId);
        }
        if (bikeId != null) {
            wrapper.eq(RentOrder::getBikeId, bikeId);
        }
        if (status != null) {
            wrapper.eq(RentOrder::getStatus, status);
        }
        wrapper.orderByDesc(RentOrder::getRentTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RentOrder rentBike(Integer userId, Integer bikeId) {
        // 1. 校验用户
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        // 2. 校验自行车
        Bike bike = bikeService.getById(bikeId);
        if (bike == null) {
            throw new BusinessException(404, "自行车不存在");
        }
        if (bike.getStatus() != null && bike.getStatus() != 1) {
            throw new BusinessException("该自行车当前不可租");
        }
        // 3. 校验用户是否有未归还订单（一个用户同一时间只能租一辆）
        LambdaQueryWrapper<RentOrder> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(RentOrder::getUserId, userId).eq(RentOrder::getStatus, 1);
        if (this.count(activeWrapper) > 0) {
            throw new BusinessException("您有未归还的自行车，请先归还");
        }
        // 4. 创建租赁订单
        RentOrder order = new RentOrder();
        order.setUserId(userId);
        order.setBikeId(bikeId);
        order.setRentTime(LocalDateTime.now());
        order.setStatus(1); // 租赁中
        order.setCost(BigDecimal.ZERO);
        this.save(order);

        // 5. 更新自行车状态为已租出（0）
        bike.setStatus(0);
        bikeService.updateById(bike);

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RentOrder returnBike(Integer orderId) {
        // 1. 校验订单
        RentOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("该订单已归还");
        }

        // 2. 计算租赁时长与费用
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(order.getRentTime(), now).toMinutes();
        if (minutes < 0) minutes = 0;
        // 每 30 分钟 1 元，不足 30 分钟按 30 分钟计（最少 1 元）
        long units = (minutes + 29) / 30;
        if (units < 1) units = 1;
        BigDecimal cost = RATE_PER_30_MIN.multiply(new BigDecimal(units)).setScale(2, RoundingMode.HALF_UP);

        // 3. 扣减用户余额
        User user = userService.getById(order.getUserId());
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (user.getBalance() == null || user.getBalance().compareTo(cost) < 0) {
            throw new BusinessException("余额不足，请先充值");
        }
        user.setBalance(user.getBalance().subtract(cost));
        userService.updateById(user);

        // 4. 更新订单
        order.setReturnTime(now);
        order.setCost(cost);
        order.setStatus(2); // 已归还
        this.updateById(order);

        // 5. 归还自行车（状态改为可用 1）
        Bike bike = bikeService.getById(order.getBikeId());
        if (bike != null) {
            bike.setStatus(1);
            bikeService.updateById(bike);
        }

        return order;
    }
}
