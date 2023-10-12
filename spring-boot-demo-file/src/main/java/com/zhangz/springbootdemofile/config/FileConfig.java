package com.zhangz.springbootdemofile.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FileConfig {
    @Value("${nontax3.vfs.minio.accessKey}")
    private String minioAccessKey;
    @Value("${nontax3.vfs.minio.bucketName}")
    private String minioBucketName;
    @Value("${nontax3.vfs.minio.region}")
    private String minioRegion;
    @Value("${nontax3.vfs.minio.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(minioRegion).credentials(minioAccessKey, minioSecretKey).build();
        createBucket(minioClient, minioBucketName);
        return minioClient;
    }
    
    /**
     * 初始化bucket
     */
    private void createBucket(MinioClient minioClient, String bucketName) {
        boolean isExist = false;
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            isExist = minioClient.bucketExists(bucketExistsArgs);
            if (!isExist) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
        } catch (Exception e) {
            log.error("创建桶【{}】失败", bucketName, e);
        }
    }

    public String getMinioBucketName() {
        return minioBucketName;
    }

}
