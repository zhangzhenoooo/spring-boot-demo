package com.zhangz.springbootdemocloudalibabaproduct.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope // 开启动态刷新 @value注入的值
@RequestMapping("/config")
public class ConfigController {
 
    @Value("${cloudAlibaba.allServer.publicConfig}")
    private String publicConfig;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/getAllServerConfig")
    public Object getAllServerConfig() {
        log.info("getAllServerConfig ");
        return JSON.toJSONString(applicationName + "：" + publicConfig);
    }
}
