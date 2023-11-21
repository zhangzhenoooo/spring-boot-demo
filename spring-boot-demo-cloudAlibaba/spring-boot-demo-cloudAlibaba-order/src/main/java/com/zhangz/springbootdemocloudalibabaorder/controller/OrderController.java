package com.zhangz.springbootdemocloudalibabaorder.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.dto.OrderDTO;
import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabacommon.vo.Result;
import com.zhangz.springbootdemocloudalibabaorder.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "订单服务")
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/getOrderById")
    public String getOrderById(String orderId) {
        log.info("getOrderById params  orderId:{}", orderId);
        List<Order> productList = orderService.getOrderById(orderId);
        return JSON.toJSONString(productList);
    }

    @PostMapping("/createOrder")
    public Result createOrder(@RequestParam("pid") String pid,@RequestParam("number") int number) throws Exception {
        log.info("getOrderById params pid:{},number:{}", pid,number);
        Order order = orderService.createOrder(pid,number);
        return Result.success(JSON.toJSONString(order));
    }
}
