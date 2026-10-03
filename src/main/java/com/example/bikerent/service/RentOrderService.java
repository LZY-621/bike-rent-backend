package com.example.bikerent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bikerent.entity.RentOrder;

import java.math.BigDecimal;

/**
 * 租赁订单 Service 接口
 */
public interface RentOrderService extends IService<RentOrder> {

    /** 分页查询（支持用户ID/自行车ID/订单状态关键字） */
    Page<RentOrder> pageRentOrder(Long pageNum, Long pageSize, Integer userId, Integer bikeId, Integer status);

    /**
     * 租车
     * @param userId 用户ID
     * @param bikeId 自行车ID
     * @return 生成的租赁订单
     */
    RentOrder rentBike(Integer userId, Integer bikeId);

    /**
     * 还车（计算费用、扣余额、更新订单与自行车状态）
     * @param orderId 订单ID
     * @return 更新后的订单（含费用）
     */
    RentOrder returnBike(Integer orderId);

    /** 计费单价：每 30 分钟 1 元 */
    BigDecimal RATE_PER_30_MIN = new BigDecimal("1");
}
