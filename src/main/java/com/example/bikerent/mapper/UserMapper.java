package com.example.bikerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bikerent.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 * 继承 BaseMapper，自动获得基础 CRUD 能力
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus BaseMapper 已提供：
    // selectById / selectOne / selectList / selectPage
    // insert / updateById / deleteById / deleteBatchIds
    // 如需自定义 SQL 方法，在此声明并在 resources/mapper/UserMapper.xml 中实现
}