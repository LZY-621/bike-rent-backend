package com.example.bikerent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 租赁订单实体 — 对应数据库表 bike_rent.rent_order
 */
@Data
@TableName("rent_order")
public class RentOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID，主键自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 所属用户ID */
    private Integer userId;

    /** 租赁自行车ID */
    private Integer bikeId;

    /** 开始租赁时间 */
    private LocalDateTime rentTime;

    /** 实际归还时间 */
    private LocalDateTime returnTime;

    /** 租赁费用 */
    private BigDecimal cost;

    /** 订单状态：1-租赁中，2-已归还 */
    private Integer status;
}
