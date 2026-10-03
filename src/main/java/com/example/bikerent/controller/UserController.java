package com.example.bikerent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bikerent.common.Result;
import com.example.bikerent.entity.User;
import com.example.bikerent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * RESTful 风格接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增用户
     * POST /api/user
     */
    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        boolean saved = userService.save(user);
        return saved ? Result.success() : Result.error("新增用户失败");
    }

    /**
     * 根据ID删除用户
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        boolean removed = userService.removeById(id);
        return removed ? Result.success() : Result.error("删除用户失败");
    }

    /**
     * 根据ID更新用户
     * PUT /api/user/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> updateById(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        boolean updated = userService.updateById(user);
        return updated ? Result.success() : Result.error("更新用户失败");
    }

    /**
     * 根据ID查询单个用户
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 查询所有用户列表
     * GET /api/user/list
     */
    @GetMapping("/list")
    public Result<List<User>> list() {
        List<User> list = userService.list();
        return Result.success(list);
    }

    /**
     * 分页查询用户
     * GET /api/user/page?pageNum=1&pageSize=10&keyword=xxx
     */
    @GetMapping("/page")
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword) {
        Page<User> page = userService.pageUser(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    /**
     * 根据用户名查询
     * GET /api/user/username/{username}
     */
    @GetMapping("/username/{username}")
    public Result<User> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 账户充值
     * POST /api/user/recharge
     * Body: {"userId": 1, "amount": 50}
     */
    @PostMapping("/recharge")
    public Result<User> recharge(@RequestBody Map<String, Object> body) {
        Integer userId = body.get("userId") == null ? null : Integer.valueOf(body.get("userId").toString());
        BigDecimal amount = body.get("amount") == null ? null : new BigDecimal(body.get("amount").toString());
        if (userId == null || amount == null) {
            return Result.error(400, "userId 和 amount 不能为空");
        }
        return Result.success("充值成功", userService.recharge(userId, amount));
    }
}