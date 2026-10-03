package com.example.bikerent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bikerent.entity.Bike;
import com.example.bikerent.mapper.BikeMapper;
import com.example.bikerent.service.BikeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 自行车 Service 实现类
 */
@Service
public class BikeServiceImpl extends ServiceImpl<BikeMapper, Bike> implements BikeService {

    @Override
    public Bike getByBikeNo(String bikeNo) {
        LambdaQueryWrapper<Bike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bike::getBikeNo, bikeNo);
        return getOne(wrapper);
    }

    @Override
    public Page<Bike> pageBike(Long pageNum, Long pageSize, String keyword) {
        Page<Bike> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Bike> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(Bike::getBikeNo, keyword)
                    .or().like(Bike::getLocation, keyword)
            );
        }
        wrapper.orderByDesc(Bike::getCreateTime);
        return page(page, wrapper);
    }
}
