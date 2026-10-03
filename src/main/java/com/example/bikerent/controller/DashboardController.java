package com.example.bikerent.controller;

import com.example.bikerent.common.Result;
import com.example.bikerent.entity.Bike;
import com.example.bikerent.entity.RentOrder;
import com.example.bikerent.entity.User;
import com.example.bikerent.mapper.BikeMapper;
import com.example.bikerent.mapper.RentOrderMapper;
import com.example.bikerent.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表盘统计接口
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BikeMapper bikeMapper;

    @Autowired
    private RentOrderMapper rentOrderMapper;

    /**
     * 获取统计数据
     * 返回：
     *   userCount      用户总数
     *   bikeIdle       空闲单车数
     *   bikeRented     已借出单车数
     *   bikeTotal      单车总数
     *   orderTotal     订单总数
     *   orderActive    租赁中订单数
     *   totalCost      累计营收
     *   monthlyOrders  按月订单分布 [{month: '2026-09', count: 10}, ...]
     *   monthlyRevenue 按月营收分布 [{month: '2026-09', amount: 100.00}, ...]
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();

        // 1. 用户统计
        List<User> users = userMapper.selectList(null);
        data.put("userCount", users.size());

        // 2. 单车统计（空闲/已借出）
        List<Bike> bikes = bikeMapper.selectList(null);
        long bikeIdle = bikes.stream().filter(b -> b.getStatus() != null && b.getStatus() == 1).count();
        long bikeRented = bikes.stream().filter(b -> b.getStatus() != null && b.getStatus() != 1).count();
        data.put("bikeIdle", bikeIdle);
        data.put("bikeRented", bikeRented);
        data.put("bikeTotal", bikes.size());

        // 3. 订单统计
        List<RentOrder> orders = rentOrderMapper.selectList(null);
        long orderActive = orders.stream().filter(o -> o.getStatus() != null && o.getStatus() == 1).count();
        BigDecimal totalCost = orders.stream()
                .map(RentOrder::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.put("orderTotal", orders.size());
        data.put("orderActive", orderActive);
        data.put("totalCost", totalCost);

        // 4. 按月订单分布（按月分组，key=年-月）
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> monthlyOrderMap = orders.stream()
                .filter(o -> o.getRentTime() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getRentTime().format(fmt),
                        Collectors.counting()
                ));
        // 排序 + 转 List
        List<Map<String, Object>> monthlyOrders = monthlyOrderMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("month", e.getKey());
                    item.put("count", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
        data.put("monthlyOrders", monthlyOrders);

        // 5. 按月营收分布
        Map<String, BigDecimal> monthlyRevenueMap = new TreeMap<>();
        for (RentOrder o : orders) {
            if (o.getRentTime() != null && o.getCost() != null) {
                String month = o.getRentTime().format(fmt);
                monthlyRevenueMap.merge(month, o.getCost(), BigDecimal::add);
            }
        }
        List<Map<String, Object>> monthlyRevenue = monthlyRevenueMap.entrySet().stream()
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("month", e.getKey());
                    item.put("amount", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
        data.put("monthlyRevenue", monthlyRevenue);

        return Result.success(data);
    }
}
