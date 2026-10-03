# 🚲 共享单车租赁管理系统 - 后端

> 基于 Spring Boot 3 + MyBatis-Plus + JWT 的共享单车租赁系统后端服务，提供 RESTful API 供前端调用。

## 📖 项目简介

共享单车租赁系统的后端服务，采用 Spring Boot 3 三层架构（Controller / Service / Mapper）实现。核心业务包括用户注册登录（BCrypt 密码加密 + JWT 无状态认证）、自行车管理、租赁订单管理、在线租车还车（自动计费）、数据统计可视化等。使用 Spring Security 统一鉴权，全局异常处理，事务保证多表更新一致性。

## 🔗 前端仓库

本项目配套前端界面：[bike-rent-frontend](https://github.com/LZY-621/bike-rent-frontend) (Vue 3 + Element Plus + ECharts)

## 💻 技术栈

| 分类 | 技术 | 版本 | 说明 |
|---|---|---|---|
| 语言 | Java | 17 | Corretto JDK 17.0.20 |
| 核心框架 | Spring Boot | 3.2.0 | 主框架，内嵌 Tomcat 10 |
| 安全认证 | Spring Security | 6.x | BCryptPasswordEncoder + 自定义 JwtAuthenticationFilter |
| ORM | MyBatis-Plus | 3.5.5 | 通用 CRUD + 分页插件 + MetaObjectHandler 自动填充 |
| 数据库 | MySQL | 8.0 | 数据库名 `bike_rent`，UTF-8 |
| JWT | jjwt-api / jjwt-impl / jjwt-jackson | 0.12.6 | HS256 签名，24 小时有效期 |
| API 文档 | springdoc-openapi-starter-webmvc-ui | 2.3.0 | OpenAPI 3 + Swagger UI |
| 工具库 | Lombok | — | `@Data` / `@Service` / `@Transactional` 等注解 |
| 构建 | Maven | — | 依赖管理 + 构建打包 |

## ✨ 功能特性

### 🔐 认证模块
- ✅ 用户注册（密码 BCrypt 加密存储）
- ✅ 用户登录（返回 JWT token + userId + username）
- ✅ 旧用户明文密码自动迁移（首次登录时静默升级为 BCrypt）
- ✅ JWT 无状态认证（前端请求自动带 `Authorization: Bearer <token>`）

### 👤 用户模块
- ✅ 用户 CRUD + 分页 + 关键字搜索
- ✅ 账户余额充值（`BigDecimal` 精确计算）

### 🚲 自行车模块
- ✅ 自行车 CRUD + 分页 + 关键字搜索
- ✅ 状态管理：1=空闲，0=已租出

### 📋 订单模块
- ✅ 租赁订单 CRUD + 分页 + 多条件筛选（userId / bikeId / status）
- ✅ **租车**：校验用户状态 → 校验单车状态 → 防重复租车 → 创建订单（事务保证原子性）
- ✅ **还车**：自动计费（每 30 分钟 1 元，最低 1 元）→ 扣余额 → 更新单车状态（事务）

### 📊 统计接口（Dashboard）
- ✅ 用户总数 / 自行车总数 / 订单总数 / 累计营收
- ✅ 自行车状态分布统计（空闲 vs 已借出）
- ✅ 按月统计订单数量
- ✅ 按月统计营收金额

### 🛡️ 基础设施
- ✅ 统一返回结构 `Result<T>`（code + message + data）
- ✅ `@RestControllerAdvice` 全局异常处理
- ✅ MyBatis-Plus `MetaObjectHandler` 自动填充 `createTime` / `updateTime`
- ✅ MyBatis-Plus `PaginationInnerInterceptor` 分页插件
- ✅ Swagger / OpenAPI 3 自动生成接口文档
- ✅ `@Transactional(rollbackFor = Exception.class)` 事务保证

## 📂 目录结构

```
bike_rent_backend/
├── src/main/java/com/example/bikerent/
│   ├── BikeRentBackendApplication.java   # 启动类
│   ├── common/                            # 公共类
│   │   ├── Result.java                    # 统一返回结构 Result<T>
│   │   └── exception/
│   │       ├── BusinessException.java     # 自定义业务异常
│   │       └── GlobalExceptionHandler.java # 全局异常处理
│   ├── config/                            # 配置类
│   │   ├── SecurityConfig.java            # Spring Security + JWT 过滤器注册
│   │   ├── JwtAuthenticationFilter.java   # JWT 请求拦截器
│   │   ├── MybatisPlusConfig.java         # MyBatis-Plus 分页插件
│   │   ├── MyMetaObjectHandler.java       # createTime / updateTime 自动填充
│   │   └── SwaggerConfig.java             # OpenAPI + JWT Bearer 方案
│   ├── controller/                        # RESTful 接口
│   │   ├── AuthController.java            # POST /api/auth/register / login
│   │   ├── UserController.java            # /api/user/**
│   │   ├── BikeController.java            # /api/bike/**
│   │   ├── RentOrderController.java       # /api/rentOrder/** + /rent + /return/{id}
│   │   └── DashboardController.java       # GET /api/dashboard/stats
│   ├── dto/                               # 请求/响应 DTO
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── LoginResponse.java
│   ├── entity/                            # 数据库实体
│   │   ├── User.java                      # @TableName("user")
│   │   ├── Bike.java                      # @TableName("bike")
│   │   └── RentOrder.java                 # @TableName("rent_order")
│   ├── mapper/                            # MyBatis-Plus BaseMapper 接口
│   │   ├── UserMapper.java
│   │   ├── BikeMapper.java
│   │   └── RentOrderMapper.java
│   ├── service/                           # Service 接口
│   │   ├── UserService.java
│   │   ├── BikeService.java
│   │   └── RentOrderService.java
│   ├── service/impl/                      # Service 实现（@Transactional）
│   │   ├── UserServiceImpl.java
│   │   ├── BikeServiceImpl.java
│   │   └── RentOrderServiceImpl.java      # 含租车/还车/计费逻辑
│   └── util/
│       └── JwtUtil.java                   # JWT 生成 / 解析 / 验证
├── src/main/resources/
│   └── application.yml                    # 数据库 + MyBatis-Plus + Server 配置
├── sql/
│   └── bike_rent_init.sql                 # 建表脚本 + 初始数据
├── pom.xml                                # Maven 依赖
└── README.md
```

## 🗄️ 数据库

### 建表脚本

```bash
# 在 MySQL console 或 Navicat 中执行
mysql -u root -p < sql/bike_rent_init.sql
```

### 数据库配置

`application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bike_rent?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 970805
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 三张核心表

| 表名 | 说明 | 关键字段 |
|---|---|---|
| user | 用户表 | id, username(唯一), password(BCrypt), balance(余额), status(状态) |
| bike | 自行车表 | id, bike_no(车辆编号), status(1空闲/0已租出) |
| rent_order | 租赁订单表 | id, user_id, bike_id, rent_time, return_time, cost, status(1租赁中/2已归还) |

## 🚀 快速开始

### 环境要求

- JDK 17+（Amazon Corretto 推荐）
- Maven 3.8+
- MySQL 8.0+

### 启动步骤

```bash
# 1. 创建数据库并执行建表脚本
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS bike_rent DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p bike_rent < sql/bike_rent_init.sql

# 2. 修改 application.yml 中的数据库密码

# 3. 启动（Maven 方式）
mvn spring-boot:run

# 或者 IDEA 中运行 BikeRentBackendApplication 主类
```

启动后访问：
- **后端接口**：http://localhost:8080
- **Swagger UI**：http://localhost:8080/swagger-ui.html （可直接在页面调试所有接口）

## 📡 接口清单

| 方法 | 路径 | 认证 | 说明 |
|---|---|---|---|
| **Auth 认证** | | | |
| POST | `/api/auth/register` | ❌ | 用户注册 |
| POST | `/api/auth/login` | ❌ | 用户登录（返回 JWT） |
| **User 用户** | | | |
| GET | `/api/user/list` | ✅ | 用户列表 |
| GET | `/api/user/page?pageNum=&pageSize=&keyword=` | ✅ | 用户分页 |
| GET | `/api/user/{id}` | ✅ | 用户详情 |
| POST | `/api/user` | ✅ | 新增用户 |
| PUT | `/api/user/{id}` | ✅ | 更新用户 |
| DELETE | `/api/user/{id}` | ✅ | 删除用户 |
| POST | `/api/user/recharge` | ✅ | 账户充值 |
| **Bike 自行车** | | | |
| GET | `/api/bike/list` | ✅ | 自行车列表 |
| GET | `/api/bike/page` | ✅ | 自行车分页 |
| GET | `/api/bike/{id}` | ✅ | 自行车详情 |
| POST | `/api/bike` | ✅ | 新增自行车 |
| PUT | `/api/bike/{id}` | ✅ | 更新自行车 |
| DELETE | `/api/bike/{id}` | ✅ | 删除自行车 |
| **RentOrder 订单** | | | |
| GET | `/api/rentOrder/page` | ✅ | 订单分页筛选 |
| POST | `/api/rentOrder/rent` | ✅ | **租车** |
| POST | `/api/rentOrder/return/{id}` | ✅ | **还车（自动计费）** |
| **Dashboard 统计** | | | |
| GET | `/api/dashboard/stats` | ❌ | 仪表盘统计数据 |

### 接口返回格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 测试账号

| 用户名 | 密码 |
|---|---|
| zhangsan | 123456 |
| lisi | 123456 |

## 💰 计费规则

- **每 30 分钟 1 元**，不足 30 分钟按 30 分钟计
- **最低 1 元**（哪怕只租 1 分钟也收 1 元）
- 还车时自动从用户余额扣除，余额不足则还车失败（事务回滚）
- 金额计算全部使用 `BigDecimal` 避免浮点数精度问题

## 🔐 认证机制

```
请求流程：
前端 Axios → 自动加 Authorization: Bearer <token>
    ↓
JwtAuthenticationFilter → 解析 token → 校验签名 → 放 SecurityContext
    ↓
Spring Security → 检查路径规则 → /api/auth/** 放行，其余需 authenticated
    ↓
Controller → 正常处理业务
```

- `SecurityConfig`：放行 `/api/auth/**`、`/api/dashboard/**`、Swagger 路径；其余 `anyRequest().authenticated()`
- `JwtAuthenticationFilter`：继承 `OncePerRequestFilter`，解析 `Authorization` 头，校验通过后构造 `UsernamePasswordAuthenticationToken` 塞入 `SecurityContextHolder`
- **旧明文密码自动迁移**：登录时判断密码是否以 `$2` 开头（BCrypt 特征），不是则为明文，比对成功后 `encode` 升级存回库

## 🔧 事务保障

`rentBike` 和 `returnBike` 方法加了 `@Transactional(rollbackFor = Exception.class)`：
- 租车：创建订单 + 更新单车状态（2 表）
- 还车：更新订单 + 扣用户余额 + 更新单车状态（3 表）

任何一步抛异常全部回滚，保证数据一致性。
