package com.zhangz.springbootdemocloudalibabaorder.service.impl;

import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getOrderById(String productName) {
        ArrayList<Order> products = new ArrayList<>();
        products.add(Order.builder().pname(productName).build());
        return products;
    }
}
