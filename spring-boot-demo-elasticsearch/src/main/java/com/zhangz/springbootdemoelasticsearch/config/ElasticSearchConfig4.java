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
// @ConditionalOnProperty(value = "es4.uri", matchIfMissing = false)
public class ElasticSearchConfig4 {

    @Value("${es.client.client-04.hosts}")
    public Set<String> hostsList;
    @Value("${es.client.client-04.username}")
    public String username;
    @Value("${es.client.client-04.password}")
    public String password;

    @Bean
    public RestClientBuilder restClientBuilder4() {
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
    public RestClient elasticsearchRestClient4(){
        return RestClient.builder(makeHttpHost()).build();
    }

    private HttpHost[] makeHttpHost() {
        if(!CollectionUtils.isEmpty(hostsList)) {
            HttpHost[] hosts = new HttpHost[hostsList.size()];
            int index = 0;
            for (String configHost : hostsList) {
                String[] split = configHost.split(":");
                hosts[index++] = new HttpHost(split[0],Integer.valueOf(split[1]));
            }
            return hosts;
        }
        throw new RuntimeException("请配置es4集群地址 ： es4.uri");
    }

    @Bean
    public RestHighLevelClient restHighLevelClient4(@Qualifier("restClientBuilder4") RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder);
    }
}
