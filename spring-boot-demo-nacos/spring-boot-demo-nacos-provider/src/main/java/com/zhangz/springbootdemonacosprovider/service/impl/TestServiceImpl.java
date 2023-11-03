package com.zhangz.springbootdemonacosprovider.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemonacosprovider.config.SystemTestProperties;
import com.zhangz.springbootdemonacosprovider.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@DubboService
public class TestServiceImpl implements TestService {

    @Value("${system.test.shardvalue}")
    private String shardvalue;

    @Autowired
    private SystemTestProperties systemTestProperties;

    @Override
    public String getShardvalue() {
        return "shardvalue";
    }

    @Override
    public Object getSystemTestProperties() {
        return JSON.toJSON(systemTestProperties);
    }
}
