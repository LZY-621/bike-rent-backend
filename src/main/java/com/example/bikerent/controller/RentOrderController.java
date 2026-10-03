package com.example.bikerent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bikerent.common.Result;
import com.example.bikerent.entity.RentOrder;
import com.example.bikerent.service.RentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 租赁订单管理控制器
 * RESTful 风格接口
 */
@RestController
@RequestMapping("/api/rentOrder")
public class RentOrderController {

    @Autowired
    private RentOrderService rentOrderService;

    /**
     * 租车
     * POST /api/rentOrder/rent
     * Body: {"userId": 1, "bikeId": 1}
     */
    @PostMapping("/rent")
    public Result<RentOrder> rent(@RequestBody Map<String, Integer> body) {
        Integer userId = body.get("userId");
        Integer bikeId = body.get("bikeId");
        if (userId == null || bikeId == null) {
            return Result.error(400, "userId 和 bikeId 不能为空");
        }
        return Result.success("租车成功", rentOrderService.rentBike(userId, bikeId));
    }

    /**
     * 还车
     * POST /api/rentOrder/return/{orderId}
     */
    @PostMapping("/return/{orderId}")
    public Result<RentOrder> returnBike(@PathVariable Integer orderId) {
        return Result.success("还车成功", rentOrderService.returnBike(orderId));
    }

    /**
     * 新增租赁订单
     * POST /api/rentOrder
     */
    @PostMapping
    public Result<Void> add(@RequestBody RentOrder rentOrder) {
        boolean saved = rentOrderService.save(rentOrder);
        return saved ? Result.success() : Result.error("新增订单失败");
    }

    /**
     * 根据ID删除订单
     * DELETE /api/rentOrder/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        boolean removed = rentOrderService.removeById(id);
        return removed ? Result.success() : Result.error("删除订单失败");
    }

    /**
     * 根据ID更新订单
     * PUT /api/rentOrder/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> updateById(@PathVariable Integer id, @RequestBody RentOrder rentOrder) {
        rentOrder.setId(id);
        boolean updated = rentOrderService.updateById(rentOrder);
        return updated ? Result.success() : Result.error("更新订单失败");
    }

    /**
     * 根据ID查询单个订单
     * GET /api/rentOrder/{id}
     */
    @GetMapping("/{id}")
    public Result<RentOrder> getById(@PathVariable Integer id) {
        RentOrder order = rentOrderService.getById(id);
        if (order == null) {
            return Result.error(404, "订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 查询所有订单列表
     * GET /api/rentOrder/list
     */
    @GetMapping("/list")
    public Result<List<RentOrder>> list() {
        return Result.success(rentOrderService.list());
    }

    /**
     * 分页查询（可按用户ID/自行车ID/订单状态筛选）
     * GET /api/rentOrder/page?pageNum=1&pageSize=10&userId=1&bikeId=1&status=1
     */
    @GetMapping("/page")
    public Result<Page<RentOrder>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer bikeId,
            @RequestParam(required = false) Integer status) {
        return Result.success(rentOrderService.pageRentOrder(pageNum, pageSize, userId, bikeId, status));
    }
}
