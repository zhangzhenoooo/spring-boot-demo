package com.zhangz.springbootdemocloudalibabaorder.service.impl;

import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaorder.service.FeignProductService;
import com.zhangz.springbootdemocloudalibabaorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private FeignProductService feignProductService;

    @Override
    public List<Order> getOrderById(String orderId) {
        ArrayList<Order> orders = new ArrayList<>();
        Object product = feignProductService.getProductByName("test feign ");

        Set<Object> producList = new HashSet<>();
        producList.add(product);
        orders.add(Order.builder().pname(orderId).products(producList).build());
        
        return orders;
    }
}
