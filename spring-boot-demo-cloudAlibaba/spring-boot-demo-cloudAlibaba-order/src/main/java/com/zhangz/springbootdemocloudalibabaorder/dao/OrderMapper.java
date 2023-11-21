package com.zhangz.springbootdemocloudalibabaorder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    Order queryById(@Param("oid") String oid);
}
