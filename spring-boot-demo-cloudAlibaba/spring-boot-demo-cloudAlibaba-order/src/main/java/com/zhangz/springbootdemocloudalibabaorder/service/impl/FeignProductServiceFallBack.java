package com.zhangz.springbootdemocloudalibabaorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaorder.service.FeignProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 100451
 *  熔断接口实现 
 *  1.容错类 必须实现需要容错的所有接口 ， 并且实现容错方案
 */
@Component
@Slf4j
public class FeignProductServiceFallBack implements FeignProductService {
    @Override
    public String getProductByName(String productName) {
        log.warn("proudct server method getProductByName fallback ,params proudctName :{} ",productName);
        Product fallback = Product.builder().pid("-1").pname("fallback").build();
        return JSON.toJSONString(fallback);
    }

    @Override
    public String getProductById(String pid) {
        log.warn("proudct server method getProductById fallback ,params pid :{} ",pid);
        Product fallback = Product.builder().pid("-1").pname("fallback").build();
        return JSON.toJSONString(fallback);
    }

    @Override
    public void reduceInventory(String pid, int num) {
       log.error("spring-cloud-product系统异常，进入熔断");
    }
}
