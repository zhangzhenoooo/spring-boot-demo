package com.zhangz.springbootdemocloudalibabaorder;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@DubboComponentScan
@MapperScan("com.zhangz.springbootdemocloudalibabaorder.dao")

public class SpringBootDemoCloudAlibabaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoCloudAlibabaOrderApplication.class, args);
    }

}
