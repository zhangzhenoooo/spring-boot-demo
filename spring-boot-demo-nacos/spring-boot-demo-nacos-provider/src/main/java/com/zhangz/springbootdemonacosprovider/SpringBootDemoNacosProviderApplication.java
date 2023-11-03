package com.zhangz.springbootdemonacosprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启nacos
@EnableDubbo
public class SpringBootDemoNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoNacosProviderApplication.class, args);
    }

}
