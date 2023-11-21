package com.zhangz.springbootdemocloudalibabaorder.service;

import com.zhangz.springbootdemocloudalibabacommon.dto.OrderDTO;
import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;

import java.util.List;

public interface OrderService {
    List<Order> getOrderById(String productName);

    Order createOrder(String pid,int number) throws Exception;
    
}
