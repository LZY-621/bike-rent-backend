-- ============================================================
-- 共享单车租赁管理系统 — 数据库初始化脚本
-- 生成日期: 2026-10-03
-- 数据库:   bike_rent (MySQL 8.0+)
-- 字符集:   utf8mb4
-- ============================================================

-- 如果数据库已存在先删除（可选，按需开启）
-- DROP DATABASE IF EXISTS bike_rent;
-- CREATE DATABASE bike_rent DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bike_rent;

-- 关闭外键检查以便 DROP 顺序无关
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. 用户表
-- ============================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          INT           NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)   NOT NULL COMMENT '用户名（唯一）',
    `password`    VARCHAR(100)  NOT NULL COMMENT '密码（BCrypt 加密存储）',
    `name`        VARCHAR(20)   DEFAULT NULL COMMENT '昵称',
    `phone`       VARCHAR(11)   DEFAULT NULL COMMENT '手机号',
    `balance`     DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    `create_time` DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 2. 自行车表
-- ============================================================
DROP TABLE IF EXISTS `bike`;
CREATE TABLE `bike` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '自行车ID',
    `bike_no`     VARCHAR(30)  NOT NULL COMMENT '自行车编号（唯一）',
    `status`      TINYINT      DEFAULT 0 COMMENT '状态：0-不可用，1-可用',
    `location`    VARCHAR(100) DEFAULT NULL COMMENT '当前位置',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_bike_no` (`bike_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自行车表';

-- ============================================================
-- 3. 租赁订单表
-- ============================================================
DROP TABLE IF EXISTS `rent_order`;
CREATE TABLE `rent_order` (
    `id`          INT           NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id`     INT           NOT NULL COMMENT '所属用户ID',
    `bike_id`     INT           NOT NULL COMMENT '租赁自行车ID',
    `rent_time`   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '开始租赁时间',
    `return_time` DATETIME      DEFAULT NULL COMMENT '实际归还时间',
    `cost`        DECIMAL(10,2) DEFAULT 0.00 COMMENT '租赁费用（元）',
    `status`      TINYINT       DEFAULT 1 COMMENT '订单状态：1-租赁中，2-已归还',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_bike_id` (`bike_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租赁订单表';

-- ============================================================
-- 测试数据（可选，用于开发调试）
-- ============================================================

-- 用户（密码 123456 的 BCrypt 哈希）
INSERT INTO `user` (`id`, `username`, `password`, `name`, `phone`, `balance`) VALUES
(1, 'zhangsan', '$2a$10$jKt6ujLcLzhvldpVrCBonegcrxy3K8sJItL/RNHs3rdqNWrv.XXL2', '张三', '13800138001', 100.00),
(2, 'lisi',     '$2a$10$5Sd4wLo.XROiAo/WA.Z2aOt6oriEt7rUErh45V/i4/fDMsx9Nsjau', '李四', '13800138002', 50.00),
(3, 'testuser', '$2a$10$xd0WDJyfGXeZCDBiXdNQYuWus42hLDX3peniagmw5xjrCLqpXXpaq', '测试用户', '13900139000', 0.00);

-- 自行车
INSERT INTO `bike` (`id`, `bike_no`, `status`, `location`) VALUES
(1, 'B001', 1, '图书馆门口'),
(2, 'B002', 1, '地铁站A口'),
(3, 'B003', 1, '科技园B栋'),
(4, 'B004', 0, '正在维修'),
(5, 'B005', 1, '万达西广场');

-- 租赁订单（全部为"已归还"，方便测试还车流程）
INSERT INTO `rent_order` (`id`, `user_id`, `bike_id`, `rent_time`, `return_time`, `cost`, `status`) VALUES
(1, 1, 1, '2026-09-15 09:00:00', '2026-09-15 10:00:00', 2.00, 2),
(2, 1, 2, '2026-09-18 14:30:00', '2026-09-18 15:15:00', 2.00, 2),
(3, 2, 3, '2026-10-01 08:00:00', '2026-10-01 08:45:00', 2.00, 2);
