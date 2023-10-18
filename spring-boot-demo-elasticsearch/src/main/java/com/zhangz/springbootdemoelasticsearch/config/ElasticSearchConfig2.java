package com.zhangz.springbootdemoelasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
// @ConditionalOnProperty(value = "es2.uri", matchIfMissing = false)
public class ElasticSearchConfig2 {

    @Value("${es.client.client-02.hosts}")
    public Set<String> hostsList;
    @Value("${es.client.client-02.username}")
    public String username;
    @Value("${es.client.client-02.password}")
    public String password;

    @Bean
    public RestClientBuilder restClientBuilder2() {
        RestClientBuilder builder = RestClient.builder(makeHttpHost());
        return builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build());
            // 3分钟
            httpClientBuilder.setKeepAliveStrategy((httpResponse, httpContext) -> TimeUnit.MINUTES.toMillis(3));
            return httpClientBuilder;
        }).setRequestConfigCallback(requestConfigBuilder -> {
            // 1分钟
            requestConfigBuilder.setConnectTimeout(60000);
            // 5分钟
            requestConfigBuilder.setSocketTimeout(300000);
            // 1分钟
            requestConfigBuilder.setConnectionRequestTimeout(60000);

            return requestConfigBuilder;
        });
    }

    @Bean
    public RestClient elasticsearchRestClient2() {
        return RestClient.builder(makeHttpHost()).build();
    }

    private HttpHost[] makeHttpHost() {
        if (!CollectionUtils.isEmpty(hostsList)) {
            HttpHost[] hosts = new HttpHost[hostsList.size()];
            int index = 0;
            for (String configHost : hostsList) {
                String[] split = configHost.split(":");
                hosts[index++] = new HttpHost(split[0], Integer.valueOf(split[1]));
            }
            return hosts;
        }
        throw new RuntimeException("请配置es2集群地址 ： es2.uri");
    }

    @Bean
    public RestHighLevelClient restHighLevelClient2(@Qualifier("restClientBuilder2") RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }
}
