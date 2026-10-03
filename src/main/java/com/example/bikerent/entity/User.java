package com.example.bikerent.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体 — 对应数据库表 bike_rent.user
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID，主键自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户名（唯一） */
    private String username;

    /** 密码 */
    private String password;

    /** 昵称 */
    @TableField("name")
    private String nickname;

    /** 手机号（11位） */
    private String phone;

    /** 账户余额 */
    private BigDecimal balance;

    /** 创建时间（自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
