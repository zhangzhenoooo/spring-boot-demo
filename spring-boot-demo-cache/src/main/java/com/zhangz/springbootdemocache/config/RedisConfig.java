package com.zhangz.springbootdemocache.config;

import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

@Slf4j
@Configuration
public class RedisConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    Environment environment;

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate() {
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }

    @Bean
    public CacheChannel channel() {
        CacheChannel channel = null;
        try {
            String configUrl = environment.resolvePlaceholders("${j2cache.config-location}");
            J2CacheConfig config = J2CacheConfig.initFromConfig(configUrl);
            J2CacheBuilder j2CacheBuilder = J2CacheBuilder.init(config);
            channel = j2CacheBuilder.getChannel();
            log.info("j2cache建立CacheChannel成功->" + "configUrl->" + configUrl + " 一级缓存->" + config.getL1CacheName() + " 二级缓存->" + config.getL2CacheName());
        } catch (IOException e) {
            log.error("j2cache建立CacheChannel报错->" + e);
        }

        return channel;
    }

}
