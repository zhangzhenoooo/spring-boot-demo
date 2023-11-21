package com.zhangz.springbootdemocloudalibabaorder.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zhangz.springbootdemocloudalibabaorder.service.impl.FeignProductServiceFallBack;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  fallback 指定容错类
 */
@RefreshScope
@Component
@FeignClient(value = "cloud-alibaba-product", url = "${cloudAlibaba.service.product.address}", fallback = FeignProductServiceFallBack.class)
public interface FeignProductService {
    // 指定调用提供者的哪个方法
    // @FeignClient+@GetMapping 就是一个完整的请求路径 http://localhost:8888/cloud-alibaba-product/getProductByName
    @GetMapping("/product/getProductByName")
    String getProductByName(@RequestParam("productName") String productName);

    @GetMapping("/product/getProductById")
    String getProductById(@RequestParam("pid") String pid);

    /**
     *  扣减库存
     * @param pid 商品ID
     * @param num 扣减数量 
     * @throws Exception
     */
    @GetMapping("/product/reduceInventory")

    void reduceInventory(@RequestParam("pid") String pid, @RequestParam("num") int num);
}
