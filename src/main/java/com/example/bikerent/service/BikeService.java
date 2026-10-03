package com.example.bikerent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bikerent.entity.Bike;

/**
 * 自行车 Service 接口
 */
public interface BikeService extends IService<Bike> {

    /** 根据自行车编号查询 */
    Bike getByBikeNo(String bikeNo);

    /** 分页查询（支持关键字：自行车编号/位置） */
    Page<Bike> pageBike(Long pageNum, Long pageSize, String keyword);
}
