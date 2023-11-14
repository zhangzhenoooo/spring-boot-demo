package com.zhangz.springbootdemocloudalibabaorder.service;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RefreshScope
@FeignClient(name = "cloud-alibaba-product",url = "${cloudAlibaba.service.product.address}")  
public interface FeignProductService {
    // 指定调用提供者的哪个方法
    // @FeignClient+@GetMapping 就是一个完整的请求路径 http://localhost:8888/cloud-alibaba-product/getProductByName
    @GetMapping("/product/getProductByName")
    String getProductByName(@RequestParam("productName") String productName);
}
