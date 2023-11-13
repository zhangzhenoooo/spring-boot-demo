package com.zhangz.springbootdemocloudalibabaorder.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/getOrderById")
    public Object getOrderById(String orderId) {
        log.info("getOrderById params  orderId:{}", orderId);
        List<Order> productList = orderService.getOrderById(orderId);
        return JSON.toJSONString(productList);
    }
}
