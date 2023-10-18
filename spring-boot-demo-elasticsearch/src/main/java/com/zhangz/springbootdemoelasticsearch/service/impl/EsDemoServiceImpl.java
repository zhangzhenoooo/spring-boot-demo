package com.zhangz.springbootdemoelasticsearch.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemocommon.exception.SystemException;
import com.zhangz.springbootdemoelasticsearch.config.EsConnectionFactory;
import com.zhangz.springbootdemoelasticsearch.service.EsDemoService;
import com.zhangz.springbootdemoelasticsearch.service.EsBaseService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = EsDemoService.class)
@Component
@Slf4j
public class EsDemoServiceImpl implements EsDemoService {

    @Autowired
    private EsBaseService esBaseService;

    @Override
    public void createIndex(@NotNull String indexName, @NotNull Integer shardNum) throws Exception {
        List<RestHighLevelClient> esCons = EsConnectionFactory.getEsCons();
        for (RestHighLevelClient esCon : esCons) {
            List<Alias> aliases = new ArrayList<>();
            aliases.add(new Alias("test_index"));
            esBaseService.createIndex(indexName, 1, aliases, esCon);
        }
    }

    @Override
    public void createMapping(String indexName) throws Exception {
        List<RestHighLevelClient> esCons = EsConnectionFactory.getEsCons();
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("properties").startObject("address").field("type", "text")
            .field("analyzer", "ik_max_word").endObject().startObject("userName").field("type", "keyword").endObject().startObject("userPhone").field("type", "text")
            .field("analyzer", "ik_max_word").endObject().endObject().endObject();

        for (RestHighLevelClient esclient : esCons) {
            esBaseService.createMapping(builder, indexName, esclient);
        }
    }

    @Override
    public void testSearch() throws IOException, BussinessException {
        String index = "index";
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        // source 过滤字段
        searchBuilder.fetchSource(new String[] {"userName", "userPhone"}, null);
        // match 匹配
        searchBuilder.query(QueryBuilders.matchQuery("userName", "哈哈"));
        // range范围查询
        searchBuilder.query(QueryBuilders.rangeQuery("userPhone").lte("15727538288").gt("15727538289"));
        // 排序
        searchBuilder.sort(new FieldSortBuilder("userName").order(SortOrder.ASC));
        // 分页
        searchBuilder.from(100);
        searchBuilder.size(10);

        List<List<Map<String, Object>>> searchResult = new ArrayList<>();
        List<RestHighLevelClient> esCons = EsConnectionFactory.getEsCons();
        for (RestHighLevelClient esCon : esCons) {
            List<Map<String, Object>> list = esBaseService.query(searchBuilder, index, esCon);
            searchResult.add(list);
        }
        log.info("查询结果|{}", JSON.toJSONString(searchResult));
    }

    @Override
    public void testAggs() throws BussinessException, SystemException {
        List<Integer> invoiceCategoryCode = Lists.newArrayList(1, 11, 12, 2, 14, 10);
        List<Integer> provinceAreaCode = Lists.newArrayList(64, 60,50);

        String startDate = "2021-01-01";
        String endDate = "2024-01-01";
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0).size(0);
        // 查询医院清单量
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.must(QueryBuilders.termQuery("invoiceTypeCode", 6)).must(QueryBuilders.termsQuery("provinceAreaCode", provinceAreaCode))
            .must(QueryBuilders.termsQuery("invoiceCategoryCode", invoiceCategoryCode));

        boolQueryBuilder.must(QueryBuilders.rangeQuery("issueDate").gte(startDate).lte(endDate));
        builder.query(boolQueryBuilder);
        AggregationBuilder aggByDay = AggregationBuilders.dateHistogram("agg_by_day").field("issueDate") // 聚合字段
            .calendarInterval(DateHistogramInterval.DAY); // 聚合间隔,1天
        AggregationBuilder aggByArea = AggregationBuilders.terms("agg_by_area").field("provinceAreaCode").size(100);
        AggregationBuilder aggByAgencyId = AggregationBuilders.terms("agg_by_agencyId").field("agencyIdCode").size(100000);
        AggregationBuilder aggByInvoiceCategoryCode = AggregationBuilders.terms("agg_by_invoiceCategoryCode").field("invoiceCategoryCode").size(10);
        aggByAgencyId.subAggregation(aggByInvoiceCategoryCode);
        aggByArea.subAggregation(aggByAgencyId);
        aggByDay.subAggregation(aggByArea);
        builder.aggregation(aggByDay);
        Aggregations aggregations = esBaseService.aggsSearch(EsConnectionFactory.getEsCon(3), "deliver", builder);
        Aggregation agg_by_day = aggregations.get("agg_by_day");
        List<? extends Histogram.Bucket> daybuckets = ((Histogram)agg_by_day).getBuckets();
        // 解析返回值
        for (Histogram.Bucket dayBucket : daybuckets) {
            String day = dayBucket.getKeyAsString();
            List<? extends Terms.Bucket> agg_by_area = ((Terms)dayBucket.getAggregations().get("agg_by_area")).getBuckets();
            for (Terms.Bucket areaBucket : agg_by_area) {
                String areaCode = areaBucket.getKeyAsString();

                List<? extends Terms.Bucket> agg_by_agencyId = ((Terms)areaBucket.getAggregations().get("agg_by_agencyId")).getBuckets();
                log.info("agg_by_agencyId.size = {}", agg_by_agencyId.size());

                for (Terms.Bucket agencyBucket : agg_by_agencyId) {
                    String agencyId = agencyBucket.getKeyAsString();

                    List<? extends Terms.Bucket> agg_by_invoiceCategoryCode = ((Terms)agencyBucket.getAggregations().get("agg_by_invoiceCategoryCode")).getBuckets();
                    log.info("agg_by_invoiceCategoryCode.size = {}", agg_by_invoiceCategoryCode.size());

                    for (Terms.Bucket icc : agg_by_invoiceCategoryCode) {
                        String keyAsString = icc.getKeyAsString();

                    }

                }
            }
        }
    }
}
