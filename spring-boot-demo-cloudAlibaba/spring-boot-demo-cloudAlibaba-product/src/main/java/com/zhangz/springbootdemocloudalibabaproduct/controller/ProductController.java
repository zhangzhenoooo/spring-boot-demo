package com.zhangz.springbootdemocloudalibabaproduct.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/getProductByName")
    public Object getProductByName(String productName) {
        log.info("getProductByName params  productName:{}", productName);
        List<Product> productList = productService.getProductByName(productName);
        return JSON.toJSONString(productList);
    }
}
