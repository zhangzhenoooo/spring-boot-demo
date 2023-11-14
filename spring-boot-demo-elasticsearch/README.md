## Elasticserach

### 1.下载与安装

### 2. 快速使用

#### 2.1 引入依赖

```xml

<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>${elasticsearch.version}</version>
</dependency>
```

#### 2.2 通过工厂模式配置es连接

```java

package com.zhangz.springbootdemoelasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author zhangz
 * @date 2022/6/21
 * @description es连接工厂
 */
@Component
@Slf4j
public class EsConnectionFactory implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static HashMap<String, RestHighLevelClient> esConns = new HashMap<>();

    private ApplicationContext context;

    public static final String RECEIVER_CLIENT_NAME = "receiverRestHighLevelClient";


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            String[] names = context.getBeanNamesForType(RestHighLevelClient.class);
            String name = names[i];
            // 排除身份证索引客户端
            if (!RECEIVER_CLIENT_NAME.equals(name)) {
                RestHighLevelClient bean = (RestHighLevelClient) context.getBean(name);
                esConns.put(name, bean);
            }
        }
    }
}

```

### 3. 基本用法

[参考文档](https://blog.csdn.net/Hmj050117/article/details/121297688)

#### 3.1 索引库操作

##### 3.1.1查询索引是否存在

kibana

```shell
#查看所有索引
GET _cat/indices
```

java

```java
    String[]idxNames=new String[]{idxName};
        GetIndexRequest request=new GetIndexRequest(idxNames);
        return client.indices().exists(request,RequestOptions.DEFAULT);
```

##### 3.1.2 删除索引

kibana

```shell
#删除索引
DELETE /index_devin2
```

java

```java
  public void deleteIndex(String idxName,RestHighLevelClient restHighLevelClient){
        try{
        if(!this.indexExist(idxName,restHighLevelClient)){
        log.error(" idxName={} 不存在！",idxName);
        return;
        }
        restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName),RequestOptions.DEFAULT);
        }catch(Exception e){
        log.error("删除索引异常|{}|{}",idxName,restHighLevelClient,e);
        throw new RuntimeException(e);
        }
        }
```

##### 3.1.3 创建索引

kibana

```shell

```

java

```java
public void createIndex(String indexName,Integer shardNum,Collection<Alias> aliases,RestHighLevelClient client)throws Exception{
        // 创建索引
        CreateIndexRequest indexRequest=new CreateIndexRequest(indexName);
        // 设置分片
        Settings settings=Settings.builder()
        // 分片
        .put("index.number_of_shards",shardNum)
        // 副本
        .put("index.number_of_replicas",shardNum).build();
        indexRequest.settings(settings);
        // 别名
        if(!CollectionUtils.isEmpty(aliases)){
        indexRequest.aliases(aliases);
        }

        IndicesClient indices=client.indices();
        CreateIndexResponse createIndexResponse=indices.create(indexRequest,RequestOptions.DEFAULT);
        if(!createIndexResponse.isAcknowledged()){
        log.error("索引创建失败|{}|{}",indexName,client);
        throw new BussinessException("索引创建失败");
        }

        }
```

##### 3.1.4总结：

> 对于索引库的操作API：  
创建索引库: CreateIndexRequest  
查询索引库：GetIndexRequest  
删除索引库：DeleteIndexRequest  
对于索引的操作是基于***IndexRequest来进行操作的。

#### 3.2 索引映射操作

##### 3.2.1 创建索引映射

kibana

```shell
PUT /wang_index_01/_mapping
{
  "properties": {
    "address": {
      "type": "text",
      "analyzer": "ik_max_word"
    },
    "userName": {
      "type": "keyword"
    },
    "userPhone": {
      "type": "text",
      "analyzer": "ik_max_word"
    }
  }
}
```

java

```java
  public void createMapping(String indexName)throws Exception{
        List<RestHighLevelClient> esCons=EsConnectionFactory.getEsCons();
        XContentBuilder builder=XContentFactory.jsonBuilder()
        .startObject()
        .startObject("properties")
        .startObject("address")
        .field("type","text")
        .field("analyzer","ik_max_word")
        .endObject()
        .startObject("userName")
        .field("type","keyword")
        .endObject()
        .startObject("userPhone")
        .field("type","text")
        .field("analyzer","ik_max_word")
        .endObject()
        .endObject()
        .endObject();

        for(RestHighLevelClient esclient:esCons){
        esIndexService.createMapping(builder,indexName,esclient);
        }
        }
```

##### 通过实体类直接创建索引

```java
@Override
public void createIndex(String indexName,Integer shardNum,Collection<Alias> aliases,Class clazz,RestHighLevelClient client)throws Exception{
        // 创建索引
        CreateIndexRequest createIndexRequest=new CreateIndexRequest(indexName);
        // 设置分片
        Settings settings=Settings.builder()
        // 分片
        .put("index.number_of_shards",shardNum)
        // 副本
        .put("index.number_of_replicas",shardNum).build();
        createIndexRequest.settings(settings);
        // 别名
        if(!CollectionUtils.isEmpty(aliases)){
        createIndexRequest.aliases(aliases);
        }
        // mapping
        XContentBuilder xContentBuilder=EsUtiles.generateBuilder(clazz);
        createIndexRequest.mapping(xContentBuilder);

        IndicesClient indices=client.indices();
        CreateIndexResponse createIndexResponse=indices.create(createIndexRequest,RequestOptions.DEFAULT);

        if(!createIndexResponse.isAcknowledged()){
        log.error("索引创建失败|{}|{}",indexName,client);
        throw new BussinessException("索引创建失败");
        }
        }


```

##### 3.2.2 查看索引映射

```shell
GET wang_index_01/_mapping
```

```java
GetMappingsRequest getMappingsRequest=new GetMappingsRequest();
        getMappingsRequest.indices("wang_index_01");
        GetMappingsResponse mapping=client.indices().getMapping(getMappingsRequest,RequestOptions.DEFAULT);
        Map<String, MappingMetadata> mappings=mapping.mappings();
        MappingMetadata metadata=mappings.get("wang_index_01");
        String s=metadata.getSourceAsMap().toString();
        System.out.println("s = "+s);
```

#### 3.3 文档操作

##### 3.3.1 新增文档

```shell
POST wang_index_01/_doc/1
{
  "address":"江西宜春上高泗溪镇",
  "userName":"张三",
  "userPhone":"15727538286"
}
```

java

```java
   public void createDocBatch(Collection<Map<String, Object>>maps,RestHighLevelClient restHighLevelClient,String index)throws BussinessException,IOException{
        BulkRequest bulkRequest=new BulkRequest();
        if(CollectionUtils.isEmpty(maps)){
        for(Map<String, Object> map:maps){
        IndexRequest indexRequest=new IndexRequest(index).id(UUIDs.base64UUID()).source(map);
        bulkRequest.add(indexRequest);
        }
        }

        RestHighLevelClient esCon=EsConnectionFactory.getEsCon("1");
        BulkResponse bulk=esCon.bulk(bulkRequest,RequestOptions.DEFAULT);
        int status=bulk.status().getStatus();
        if(RestStatus.OK.getStatus()==status){
        log.error("文档上传失败|{}|{}|{}",maps,index,restHighLevelClient);
        throw new BussinessException("文档上传失败");
        }
        }
```

##### 3.3.2 查询文档

kibana

```shell
GET wang_index_01/_doc/1
```

java

```java
    public String queryByDocId(RestHighLevelClient restHighLevelClient,String index,String id)throws IOException{
        GetRequest getRequest=new GetRequest(index);
        getRequest.id(id);
        GetResponse documentFields=restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        String s=documentFields.toString();
        return s;
        }
```

##### 3.3.3 删除文档

kibana

```shell
DELETE wang_index_01/_doc/1
```

java

```java
 public void deleteByDocId(RestHighLevelClient restHighLevelClient,String index,String id)throws IOException{
        DeleteRequest deleteRequest=new DeleteRequest(index);
        deleteRequest.id(id);
        restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        }
```

##### 3.3.3 更新文档

kibana

```shell
PUT wang_index_01/_doc/1
{
  
  "address":"江西宜春上高泗溪镇",
  "userName":"哈哈",
  "userPhone":"15727538288"
 
}
```

java

```java
  public void modifyByDocId(RestHighLevelClient restHighLevelClient,String index,String id,Map<String, Object> doc)throws IOException{
        UpdateRequest updateRequest=new UpdateRequest(index,index);
        updateRequest.doc(doc);
        restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
        }
```

#### 3.4 查询

##### 3.4.1 search

注意，排序的字段一定不能是可分词的，不然会出现如下错误:
Text fields are not optimised for operations that require per-document field data like aggregations and sorting, so
these operations are disabled by default. Please use a keyword field instead

kibana

```shell
GET wang_index_01/_search

GET deliver/_search
{
  "size": 20, 
  "from": 0, 
  "sort": [
    {
      "agencyIdCode.keyword": {
        "order": "desc"
      }
    }
  ], 
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "payerName.keyword": {
              "value": "栓Q",
              "boost": 1
            }
          }
        },
        {
          "terms": {
            "receiverInfo.type": [
              "1201"
            ],
            "boost": 1
          }
        },
        {
          "term": {
            "receiverInfo.value.keyword": {
              "value": "18810425281",
              "boost": 1
            }
          }
        },
        {
          "terms": {
            "agencyIdCode": [
              "f75b912a937f50115dca18541229c457"
            ],
            "boost": 1
          }
        }
      ],
      "adjust_pure_negative": true,
      "boost": 1
    }
  }
}

```

java

```java
  public void search()throws IOException,BussinessException{
        String index="index";
        SearchSourceBuilder searchBuilder=new SearchSourceBuilder();
        // source 过滤字段
        searchBuilder.fetchSource(new String[]{"userName","userPhone"},null);
        // match 匹配
        searchBuilder.query(QueryBuilders.matchQuery("userName","哈哈"));
        // range范围查询
        searchBuilder.query(QueryBuilders.rangeQuery("userPhone").lte("15727538288").gt("15727538289"));
        // 排序
        searchBuilder.sort(new FieldSortBuilder("userName").order(SortOrder.ASC));
        // 分页
        searchBuilder.from(100);
        searchBuilder.size(10);

        List<List<Map<String, Object>>>searchResult=new ArrayList<>();
        List<RestHighLevelClient> esCons=EsConnectionFactory.getEsCons();
        for(RestHighLevelClient esCon:esCons){
        List<Map<String, Object>>list=esBaseService.query(searchBuilder,index,esCon);
        searchResult.add(list);
        }
        log.info("查询结果|{}",JSON.toJSONString(searchResult));
        }
```

##### 3.4.2 aggs metrics 聚合之度量

ps:量等同于mysql里面的求最大值，最小值，平均值，求和，统计数量等.

```shell
GET goods/_search
{
  "query": {//查询部分
    "match": {
      "title": "手机"
    }
  },
  "aggs": {//聚合部分
    "max_price": {//聚合结果别名
      "max": {//指标聚合类型
        "field": "price"//字段类型要为number
      }
    }
  }
}
```

##### 3.4.3 aggs bucket 聚合之桶

ps:每一个Bucket （桶）是按照我们定义的准则去判断数据是否会落入桶（bucket）中。一个单独的响应中，bucket（桶）的最大个数默认是10000，我们可以通过serarch.max_buckets去进行调整。 Bucket
聚合查询就像是数据库中的group by.  
kibana

```shell
GET deliver/_search
{
  "from": 0,
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "invoiceTypeCode": {
              "value": 6,
              "boost": 1
            }
          }
        },
        {
          "terms": {
            "invoiceCategoryCode": [
              1,
              12,
              2,
              14
            ],
            "boost": 1
          }
        },
        {
          "range": {
            "commitTime": {
              "from": "2022-09-01 00:00:00",
              "to": "2024-09-30 00:00:00",
              "include_lower": true,
              "include_upper": true,
              "boost": 1
            }
          }
        }
      ],
      "adjust_pure_negative": true,
      "boost": 1
    }
  },
  "aggregations": {
    "group_by_day": {
      "date_histogram": {
        "field": "commitTime",
        "calendar_interval": "1d", // 每隔一天统计
        "offset": 0,
        "order": {
          "_key": "asc"
        },
        "keyed": false,
        "format": "yyyy-MM", //日期展示按照  年月展示,
        "min_doc_count": 0 // 最小统计数量，可通过该值过滤少于*的值
      },
      "aggregations": {
        "agg_by_area": {
          "terms": {
            "field": "provinceAreaCode",
            "size": 33,
            "min_doc_count": 1,
            "shard_min_doc_count": 0,
            "show_term_doc_count_error": false,
            "order": [
              {
                "_count": "desc"
              },
              {
                "_key": "asc"
              }
            ]
          },
          "aggregations": {
            "agg_by_agencyId": {
              "terms": {
                "field": "agencyIdCode",
                "size": 10000,
                "min_doc_count": 1,
                "shard_min_doc_count": 0,
                "show_term_doc_count_error": false,
                "order": [
                  {
                    "_count": "desc"
                  },
                  {
                    "_key": "asc"
                  }
                ]
              },
              "aggregations": {
                "agg_by_agencyName": {
                  "terms": {
                    "field": "agencyName.keyword",
                    "size": 150,
                    "min_doc_count": 1,
                    "shard_min_doc_count": 0,
                    "show_term_doc_count_error": false,
                    "order": [
                      {
                        "_count": "desc"
                      },
                      {
                        "_key": "asc"
                      }
                    ]
                  },
                  "aggregations": {
                    "agg_by_invoiceCategoryCode": {
                      "terms": {
                        "field": "invoiceCategoryCode",
                        "size": 4,
                        "min_doc_count": 1,
                        "shard_min_doc_count": 0,
                        "show_term_doc_count_error": false,
                        "order": [
                          {
                            "_count": "desc"
                          },
                          {
                            "_key": "asc"
                          }
                        ]
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
```

java

```java
public void getAggs()throws BussinessException,SystemException{
        List<Integer> invoiceCategoryCode=Lists.newArrayList(1,11,12,2,14,10);
        String startDate="2021-01-01";
        String endDate="2024-01-01";
        SearchSourceBuilder builder=new SearchSourceBuilder();
        builder.from(0).size(0);
        // 查询医院清单量
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        int provinceAreaCode=64;
        boolQueryBuilder.must(QueryBuilders.termQuery("invoiceTypeCode",6)).must(QueryBuilders.termsQuery("provinceAreaCode",provinceAreaCode))
        .must(QueryBuilders.termsQuery("invoiceCategoryCode",invoiceCategoryCode));

        boolQueryBuilder.must(QueryBuilders.rangeQuery("issueDate").gte(startDate).lte(endDate));
        builder.query(boolQueryBuilder);
        AggregationBuilder aggByDay=AggregationBuilders.dateHistogram("agg_by_day").field("issueDate") // 聚合字段
        .calendarInterval(DateHistogramInterval.DAY); // 聚合间隔,1天
        AggregationBuilder aggByArea=AggregationBuilders.terms("agg_by_area").field("provinceAreaCode").size(100);
        AggregationBuilder aggByAgencyId=AggregationBuilders.terms("agg_by_agencyId").field("agencyIdCode").size(100000);
        AggregationBuilder aggByInvoiceCategoryCode=AggregationBuilders.terms("agg_by_invoiceCategoryCode").field("invoiceCategoryCode").size(10);
        aggByAgencyId.subAggregation(aggByInvoiceCategoryCode);
        aggByArea.subAggregation(aggByAgencyId);
        aggByDay.subAggregation(aggByArea);
        builder.aggregation(aggByDay);
        Aggregations aggregations=esBaseService.aggsSearch(EsConnectionFactory.getEsCon("1"),"deliver",builder);
        Aggregation agg_by_day=aggregations.get("agg_by_day");
        List<?extends Histogram.Bucket> daybuckets=((Histogram)agg_by_day).getBuckets();
        // 解析返回值
        for(Histogram.Bucket dayBucket:daybuckets){
        String day=dayBucket.getKeyAsString();
        List<?extends Terms.Bucket> agg_by_area=((Terms)dayBucket.getAggregations().get("agg_by_area")).getBuckets();
        for(Terms.Bucket areaBucket:agg_by_area){
        String areaCode=areaBucket.getKeyAsString();

        List<?extends Terms.Bucket> agg_by_agencyId=((Terms)areaBucket.getAggregations().get("agg_by_agencyId")).getBuckets();
        log.info("agg_by_agencyId.size = {}",agg_by_agencyId.size());

        for(Terms.Bucket agencyBucket:agg_by_agencyId){
        String agencyId=agencyBucket.getKeyAsString();

        List<?extends Terms.Bucket> agg_by_invoiceCategoryCode=((Terms)agencyBucket.getAggregations().get("agg_by_invoiceCategoryCode")).getBuckets();
        log.info("agg_by_invoiceCategoryCode.size = {}",agg_by_invoiceCategoryCode.size());

        for(Terms.Bucket icc:agg_by_invoiceCategoryCode){
        String keyAsString=icc.getKeyAsString();

        }

        }
        }
        }
        }
```

#### 3.5 索引

##### 3.5.1 查询别名

```shell
GET indexName/_alias
```

##### 3.5.2 新增别名 batch

```shell
批量设置别名
POST _aliases
{
  "actions": [
    {
      "add": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver"
      }
    },
    {
      "add": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_40_41_2023"
      }
    },
     {
      "add": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_40_41_20236"
      }
    },
    {
      "add": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_41__2023"
      }
    },
    {
      "add": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_41__20236"
      }
    },
#    删除别名
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver___20236"
      }
    }
  ]
}
```

##### 3.5.3 删除别名

```shell
批量删除别名
POST _aliases
{
  "actions": [
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver"
      }
    },
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_40_41_2023"
      }
    },
     {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_40_41_20236"
      }
    },
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_41__2023"
      }
    },
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver_41__20236"
      }
    },
    {
      "remove": {
        "index": "deliver_40_41_20236_0",
        "alias": "deliver___20236"
      }
    } 
  ]
}
```

##### 3.5.4 索引新增字段

```shell
#给满足以下格式的索引增加字段
PUT deliver_*_*_*_0/_mapping
{
  "properties": {
    "settlementNumber": {
      "type": "keyword"
    },
    "settlementDate": {
      "type": "date",
      "format": "yyyyMMdd||yyyy-MM-dd"
    },
    "custom9": {
      "type": "keyword"
    },
    "custom10": {
      "type": "keyword"
    }
  }
}
```

#### 4. 分词器


