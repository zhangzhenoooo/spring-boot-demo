package com.zhangz.springbootdemonacosprovider.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemonacosprovider.config.SystemTestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController {

    @Value("${system.test.shardvalue}")
    private String shardvalue;

    @Autowired
    private SystemTestProperties systemTestProperties;

    @PostMapping("getShardValue")
    public String getShardvalue() {
        return shardvalue;
    }

    @PostMapping("getSystemTestProperties")
    public Object getSystemTestProperties() {
        return JSON.toJSON(systemTestProperties);
    }

}
