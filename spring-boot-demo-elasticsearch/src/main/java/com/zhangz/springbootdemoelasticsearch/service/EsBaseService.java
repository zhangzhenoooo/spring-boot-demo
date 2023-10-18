package com.zhangz.springbootdemoelasticsearch.service;

import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemocommon.exception.SystemException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EsBaseService {

    boolean indexExist(String idxName, RestHighLevelClient client) throws Exception;

    /**
     * 删除index
     */
    void deleteIndex(String idxName, RestHighLevelClient restHighLevelClient);

    void createIndex(String indexName, Integer shardNum, Collection<Alias> aliases, RestHighLevelClient client) throws Exception;

    void createIndex(String indexName, Integer shardNum, Collection<Alias> aliases, Class clazz, RestHighLevelClient client) throws Exception;

    void createMapping(XContentBuilder xContentBuilder, String indexName, RestHighLevelClient client) throws BussinessException, IOException;

    void createMapping(RestHighLevelClient client, String indexName, Class clazz) throws IOException, BussinessException;

    void createDocBatch(Collection<Map<String, Object>> maps, RestHighLevelClient restHighLevelClient, String index) throws BussinessException, IOException;

    void deleteByDocId(RestHighLevelClient restHighLevelClient, String index, String id) throws IOException;

    String queryByDocId(RestHighLevelClient restHighLevelClient, String index, String id) throws IOException;

    void modifyByDocId(RestHighLevelClient restHighLevelClient, String index, String id, Map<String, Object> doc) throws IOException;

    List<Map<String, Object>> query(SearchSourceBuilder searchSourceBuilder, String index, RestHighLevelClient restHighLevelClient) throws IOException;

    Aggregations aggsSearch(RestHighLevelClient restHighLevelClient, String index, SearchSourceBuilder searchSourceBuilder) throws SystemException;
}
