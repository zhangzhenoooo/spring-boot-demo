package com.zhangz.springbootdemoelasticsearch.service;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface EsBaseService {

    void createIndex(String indexName, Integer shardNum) throws Exception;

    void createMapping( String indexName) throws Exception;

}
