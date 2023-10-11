package com.zhangz.springbootdemostarter.config;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.remoting.transport.dispatcher.message.MessageOnlyDispatcher;
import com.zhangz.springbootdemofile.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** dubbo 服务提供者注册类
 * @author 100451
 */
@Configuration
public class DubboProviderConfig {

    private static final Integer RETRIES = 0;
    private static final Integer TIMEOUT = 60000;
    private static final Integer SHORTER_TIMEOUT = 10000;

    @Value("${spring.dubbo.registry}")
    private String dubboRegistryAddress;
    @Value("${spring.dubbo.protocol.port}")
    private Integer dubboProtocolPort;

    // @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("demoDubbo");
        return applicationConfig;
    }

    // @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPayload(52428800);
        protocolConfig.setPort(dubboProtocolPort);
        protocolConfig.setThreads(1000);
        protocolConfig.setDispatcher(MessageOnlyDispatcher.NAME);
        return protocolConfig;
    }

    // @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setId("payload");
        providerConfig.setPayload(52428800);
        return providerConfig;
    }

    // @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId("providerRegistry");
        registryConfig.setAddress(dubboRegistryAddress);
        // 设置cache缓存，解决启动警告提醒
        registryConfig.setFile("cache/demoDubboProvider.cache");
        return registryConfig;
    }

    @Autowired
    private MinIOService minIOService;

    @Bean
    public ServiceConfig<MinIOService> checkServiceConfig() {
        ServiceConfig<MinIOService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setRetries(RETRIES);
        serviceConfig.setInterface(MinIOService.class);
        serviceConfig.setRef(minIOService);
        serviceConfig.setRegistry(registryConfig());
        serviceConfig.setProvider(providerConfig());
        serviceConfig.setApplication(applicationConfig());
        serviceConfig.setProtocol(protocolConfig());
        serviceConfig.export();
        serviceConfig.setTimeout(TIMEOUT);
        return serviceConfig;
    }

}
