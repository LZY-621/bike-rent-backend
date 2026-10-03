package com.example.bikerent.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 自行车实体 — 对应数据库表 bike_rent.bike
 */
@Data
@TableName("bike")
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 自行车编号（唯一） */
    private String bikeNo;

    /** 状态：0-不可用，1-可用 */
    private Integer status;

    /** 当前位置 */
    private String location;

    /** 创建时间（自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
