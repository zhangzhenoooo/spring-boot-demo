package com.zhangz.springbootdemocloudalibabaproduct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    Product queryById(@Param("pid") String pid);
}
