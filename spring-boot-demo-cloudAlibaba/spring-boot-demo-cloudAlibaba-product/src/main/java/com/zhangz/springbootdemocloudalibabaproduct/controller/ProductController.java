package com.zhangz.springbootdemocloudalibabaproduct.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @SentinelResource(value = "product.getProductByName", blockHandler = "productCommonHandler")
    public String getProductByName(@RequestParam("productName") String productName) {
        log.info("getProductByName params  productName:{}", productName);
        List<Product> productList = productService.getProductByName(productName);
        return JSON.toJSONString(productList);
    }

    @GetMapping("/getProductById")
    @ResponseBody
    @SentinelResource(value = "product.getProductById", blockHandler = "productCommonHandler")
    public String getProductById(@RequestParam("pid") String pid) throws Exception {
        log.info("getProductByName params  pid:{}", pid);
        if ("-1".equals(pid)) {
            throw new Exception("测试熔断");
        }

        Product p = productService.getProductById(pid);
        return JSON.toJSONString(p);
    }

    @GetMapping("/reduceInventory")
    @ResponseBody
    @SentinelResource(value = "product.getProductByName", blockHandler = "productCommonHandler")
    public void reduceInventory(@RequestParam("pid") String pid,@RequestParam("num") int num) throws Exception {
        log.info("调用减库存 ，商品ID：{}，数量“{}", pid, num);
         productService.reduceInventory(pid,num);
     }

    public String productCommonHandler(HttpServletRequest request, BlockException blockException) {
        log.error("接口【{}】调用失败", request.getRequestURI());
        return "Sentinel流控，调用失败";
    }
}
