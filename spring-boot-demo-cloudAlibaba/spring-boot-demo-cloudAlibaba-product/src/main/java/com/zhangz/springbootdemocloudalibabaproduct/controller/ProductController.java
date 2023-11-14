package com.zhangz.springbootdemocloudalibabaproduct.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/product")
@Api(tags = "商品服务")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/getProductByName")
    @ResponseBody
    public String getProductByName(@RequestParam("productName") String productName) {
        log.info("getProductByName params  productName:{}", productName);
        List<Product> productList = productService.getProductByName(productName);
        return JSON.toJSONString(productList);
    }
}
