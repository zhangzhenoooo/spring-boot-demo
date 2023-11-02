package com.zhangz.springbootdemoconfmanager;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启nacos
@EnableDubbo(scanBasePackages = "com.zhangz.springbootdemoconfmanager.service")
public class SpringBootDemoConfManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoConfManagerApplication.class, args);
    }

}
