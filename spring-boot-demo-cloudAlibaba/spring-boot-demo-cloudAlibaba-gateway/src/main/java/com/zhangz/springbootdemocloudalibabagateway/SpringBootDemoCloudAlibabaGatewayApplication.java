package com.zhangz.springbootdemocloudalibabagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringBootDemoCloudAlibabaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoCloudAlibabaGatewayApplication.class, args);
    }

}
