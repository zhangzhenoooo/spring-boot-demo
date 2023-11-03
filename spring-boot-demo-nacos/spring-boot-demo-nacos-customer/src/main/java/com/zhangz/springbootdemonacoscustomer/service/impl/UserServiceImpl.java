package com.zhangz.springbootdemonacoscustomer.service.impl;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.zhangz.springbootdemonacoscustomer.service.UserService;
import com.zhangz.springbootdemonacosprovider.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class UserServiceImpl implements UserService {
    @DubboReference
    private TestService testService;

    @Override
    public Object getTestServiceShardValue(String str) {
        return testService.getSystemTestProperties() + str;
    }
}
