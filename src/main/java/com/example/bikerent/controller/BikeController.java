package com.example.bikerent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bikerent.common.Result;
import com.example.bikerent.entity.Bike;
import com.example.bikerent.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自行车管理控制器
 * RESTful 风格接口
 */
@RestController
@RequestMapping("/api/bike")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    /**
     * 新增自行车
     * POST /api/bike
     */
    @PostMapping
    public Result<Void> add(@RequestBody Bike bike) {
        boolean saved = bikeService.save(bike);
        return saved ? Result.success() : Result.error("新增自行车失败");
    }

    /**
     * 根据ID删除自行车
     * DELETE /api/bike/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        boolean removed = bikeService.removeById(id);
        return removed ? Result.success() : Result.error("删除自行车失败");
    }

    /**
     * 根据ID更新自行车
     * PUT /api/bike/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> updateById(@PathVariable Integer id, @RequestBody Bike bike) {
        bike.setId(id);
        boolean updated = bikeService.updateById(bike);
        return updated ? Result.success() : Result.error("更新自行车失败");
    }

    /**
     * 根据ID查询单个自行车
     * GET /api/bike/{id}
     */
    @GetMapping("/{id}")
    public Result<Bike> getById(@PathVariable Integer id) {
        Bike bike = bikeService.getById(id);
        if (bike == null) {
            return Result.error(404, "自行车不存在");
        }
        return Result.success(bike);
    }

    /**
     * 查询所有自行车列表
     * GET /api/bike/list
     */
    @GetMapping("/list")
    public Result<List<Bike>> list() {
        return Result.success(bikeService.list());
    }

    /**
     * 分页查询（支持关键字：自行车编号/位置）
     * GET /api/bike/page?pageNum=1&pageSize=10&keyword=B001
     */
    @GetMapping("/page")
    public Result<Page<Bike>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(bikeService.pageBike(pageNum, pageSize, keyword));
    }
}
