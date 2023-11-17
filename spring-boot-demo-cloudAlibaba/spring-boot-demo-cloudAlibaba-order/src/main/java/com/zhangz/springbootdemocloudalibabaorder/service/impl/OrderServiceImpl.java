package com.zhangz.springbootdemocloudalibabaorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.zhangz.springbootdemocloudalibabacommon.entity.Order;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabacommon.service.ProductRpcService;
import com.zhangz.springbootdemocloudalibabaorder.config.MQConfig;
import com.zhangz.springbootdemocloudalibabaorder.service.FeignProductService;
import com.zhangz.springbootdemocloudalibabaorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private FeignProductService feignProductService;

    @DubboReference
    private ProductRpcService productRPCService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Order> getOrderById(String orderId) {
        ArrayList<Order> orders = new ArrayList<>();
        String productStr = feignProductService.getProductByName("test feign ");
        Product product = JSON.parseObject(productStr, Product.class);
        Order order = Order.builder().oid(orderId).pname(product.getPname()).number(1).pprice(12.5).build();
        orders.add(order);
        return orders;
    }

    @Override
    public Order createOrder(String pid) throws Exception {
        String orderId = UuidUtils.generateUuid();

        Product productById = productRPCService.getProductById(pid);
        log.info("dubbo 查询商品服务返回商品信息|{}", productById);

        String productStr = feignProductService.getProductById(pid);
        log.info("查询商品服务返回商品信息|{}", productStr);
        Product product = JSON.parseObject(productStr, Product.class);

        if ("-1".equals(product.getPid())) {
            return Order.builder().oid(orderId).pid("-1").pname("熔断啦").build();
        }

        Order build = Order.builder().oid(orderId).pid(pid).pname(product.getPname()).build();

        log.info("生成订单信息|{}", JSON.toJSONString(build));
        // 模拟一次网络延时
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 订单生成以后 发送消息通知用户 已下单
        rabbitTemplate.convertAndSend(MQConfig.ORDER_EXCHANGE, MQConfig.ORDER_RUTEKEY, "订单【" + orderId + "】已下单！");
        return build;
    }
 
}
