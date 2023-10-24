package com.zhangz.springbootdemoelasticsearch.service;

import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemocommon.exception.SystemException;

import java.io.IOException;

public interface EsDemoService {

    void createIndex(String indexName, Integer shardNum) throws Exception;

    void createMapping(String indexName) throws Exception;

    void testSearch() throws IOException, BussinessException;

    void testAggs() throws BussinessException, SystemException;
}
