package com.zhangz.springbootdemofile.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
public class MinioProperties {

    @Value("${nontax3.vfs.minio.region}")
    private String endpoint;

    @Value("${nontax3.vfs.minio.accessKey}")
    private String accessKey;

    @Value("${nontax3.vfs.minio.secretKey}")
    private String accessSecret;

    @Value("${nontax3.vfs.minio.bucketName}")
    private String bucket;
}
