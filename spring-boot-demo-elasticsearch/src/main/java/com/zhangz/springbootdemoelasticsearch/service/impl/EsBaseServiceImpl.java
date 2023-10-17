package com.zhangz.springbootdemoelasticsearch.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemoelasticsearch.config.EsConnectionFactory;
import com.zhangz.springbootdemoelasticsearch.service.EsBaseService;
import com.zhangz.springbootdemoelasticsearch.service.EsIndexService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = EsBaseService.class)
@Component
@Slf4j
public class EsBaseServiceImpl implements EsBaseService {

    @Autowired
    private EsIndexService esIndexService;

    @Override
    public void createIndex(@NotNull String indexName, @NotNull Integer shardNum) throws Exception {
        List<RestHighLevelClient> esCons = EsConnectionFactory.getEsCons();
        for (RestHighLevelClient esCon : esCons) {
            List<Alias> aliases = new ArrayList<>();
            aliases.add(new Alias("test_index"));
            esIndexService.createIndex(indexName, 1, aliases, esCon);
        }
    }

    @Override
    public void createMapping( String indexName) throws Exception {
          List<RestHighLevelClient> esCons = EsConnectionFactory.getEsCons();
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("address")
                .field("type", "text")
                .field("analyzer", "ik_max_word")
                .endObject()
                .startObject("userName")
                .field("type", "keyword")
                .endObject()
                .startObject("userPhone")
                .field("type", "text")
                .field("analyzer", "ik_max_word")
                .endObject()
                .endObject()
                .endObject();
 
        for (RestHighLevelClient esclient : esCons) {
            esIndexService.createMapping(builder,indexName, esclient);
        }
    }
}
