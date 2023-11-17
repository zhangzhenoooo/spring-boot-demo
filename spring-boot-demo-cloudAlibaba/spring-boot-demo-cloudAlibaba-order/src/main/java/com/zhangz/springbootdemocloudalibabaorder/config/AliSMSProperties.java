package com.zhangz.springbootdemocloudalibabaorder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:cloudAlibaba.properties")
@ConfigurationProperties(prefix = "ali.cloud.sms")
public class AliSMSProperties {

    private String accessKeyId;
    private String accessKeySecret;

}
