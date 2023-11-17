package com.zhangz.springbootdemocloudalibabaproduct.service.rpc;

import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabacommon.service.ProductRpcService;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(interfaceClass = ProductRpcService.class)
@Slf4j
public class ProductRpcServiceImpl implements ProductRpcService {

    @Resource
    private ProductService productService;

    @Override
    public List<Product> getProductByName(String productName) {
        log.info("进入 PRC 调用 getProductByName ");
        
        return productService.getProductByName(productName);
    }

    @Override
    public Product getProductById(String pid) {
        log.info("进入 PRC 调用 getProductById ");

        return productService.getProductById(pid);
    }
}
