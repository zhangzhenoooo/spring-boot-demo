package com.zhangz.springbootdemoelasticsearch.service.impl;

import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemoelasticsearch.config.EsConnectionFactory;
import com.zhangz.springbootdemoelasticsearch.config.EsFieldEnum;
import com.zhangz.springbootdemoelasticsearch.service.EsIndexService;
import com.zhangz.springbootdemoelasticsearch.utils.EsUtiles;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EsIndexServiceImpl implements EsIndexService {

    @Override
    public boolean indexExist(String idxName, RestHighLevelClient client) throws Exception {
        String[] idxNames = new String[] {idxName};
        GetIndexRequest request = new GetIndexRequest(idxNames);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    @Override
    /**
     * 删除index
     */
    public void deleteIndex(String idxName, RestHighLevelClient restHighLevelClient) {
        try {
            if (!this.indexExist(idxName, restHighLevelClient)) {
                log.error(" idxName={} 不存在！", idxName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("删除索引异常|{}|{}", idxName, restHighLevelClient, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createIndex(String indexName, Integer shardNum, Collection<Alias> aliases, RestHighLevelClient client) throws Exception {
        // 创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        // 设置分片
        Settings settings = Settings.builder()
            // 分片
            .put("index.number_of_shards", shardNum)
            // 副本
            .put("index.number_of_replicas", shardNum).build();
        createIndexRequest.settings(settings);
        // 别名
        if (!CollectionUtils.isEmpty(aliases)) {
            createIndexRequest.aliases(aliases);
        }

        IndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        if (!createIndexResponse.isAcknowledged()) {
            log.error("索引创建失败|{}|{}", indexName, client);
            throw new BussinessException("索引创建失败");
        }

    }

    @Override
    public void createIndex(String indexName, Integer shardNum, Collection<Alias> aliases, Class clazz, RestHighLevelClient client) throws Exception {
        // 创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        // 设置分片
        Settings settings = Settings.builder()
            // 分片
            .put("index.number_of_shards", shardNum)
            // 副本
            .put("index.number_of_replicas", shardNum).build();
        createIndexRequest.settings(settings);
        // 别名
        if (!CollectionUtils.isEmpty(aliases)) {
            createIndexRequest.aliases(aliases);
        }
        // mapping
        XContentBuilder xContentBuilder = EsUtiles.generateBuilder(clazz);
        createIndexRequest.mapping(xContentBuilder);

        IndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);

        if (!createIndexResponse.isAcknowledged()) {
            log.error("索引创建失败|{}|{}", indexName, client);
            throw new BussinessException("索引创建失败");
        }
    }

    @Override
    public void createMapping(XContentBuilder xContentBuilder, String indexName, RestHighLevelClient client) throws BussinessException, IOException {
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.source(xContentBuilder);

        AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
        if (!acknowledgedResponse.isAcknowledged()) {
            log.error("索引mapping创建失败|{}|{}|{}", indexName, client, xContentBuilder);
            throw new BussinessException("索引创建失败");
        }

    }

    @Override
    public void createMapping(RestHighLevelClient client, String indexName, Class clazz) throws IOException, BussinessException {
        XContentBuilder xContentBuilder = EsUtiles.generateBuilder(clazz);
        this.createMapping(xContentBuilder, indexName, client);
    }

    @Override
    public void createDocBatch(Collection<Map<String, Object>> maps, RestHighLevelClient restHighLevelClient, String index) throws BussinessException, IOException {
        BulkRequest bulkRequest = new BulkRequest();
        if (CollectionUtils.isEmpty(maps)) {
            for (Map<String, Object> map : maps) {
                IndexRequest indexRequest = new IndexRequest(index).id(UUIDs.base64UUID()).source(map);
                bulkRequest.add(indexRequest);
            }
        }

        RestHighLevelClient esCon = EsConnectionFactory.getEsCon("1");
        BulkResponse bulk = esCon.bulk(bulkRequest, RequestOptions.DEFAULT);
        int status = bulk.status().getStatus();
        if (RestStatus.OK.getStatus() == status) {
            log.error("文档上传失败|{}|{}|{}", maps, index, restHighLevelClient);
            throw new BussinessException("文档上传失败");
        }
    }

    @Override
    public void deleteByDocId(RestHighLevelClient restHighLevelClient, String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index);
        deleteRequest.id(id);
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public String queryByDocId(RestHighLevelClient restHighLevelClient, String index, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index);
        getRequest.id(id);
        GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String s = documentFields.toString();
        return s;
    }

    @Override
    public void modifyByDocId(RestHighLevelClient restHighLevelClient, String index, String id, Map<String, Object> doc) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, index);
        updateRequest.doc(doc);
        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
     }
}
