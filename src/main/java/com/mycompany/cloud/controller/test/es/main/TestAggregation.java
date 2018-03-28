package com.mycompany.cloud.controller.test.es.main;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by zhangjuntao on 2017/4/10.
 *
 * 聚合测试
 */
public class TestAggregation {


    public static void main( String[] args )throws Exception {

        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                /*.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));*/

        //client.listedNodes().stream().forEach(System.out::println);



        //insertDoc(client);
        search(client);
        aggregationQuery(client);

        client.close();
        System.out.println( "Hello World!" );
    }

    /**
     * 不能得到结果
     * @param client
     * @throws Exception
     */
    public static void aggregationQuery(TransportClient client)throws Exception{
        QueryBuilder qb = termQuery("env", "test");
        SearchResponse sr = client.prepareSearch()
                .setQuery(qb)
                .addAggregation(AggregationBuilders.terms("age").field("age")
                        /*.subAggregation(
                            AggregationBuilders.topHits("top")
                                    .explain(true)
                                    .size(3)
                                    .from(0))*/
                ).addAggregation(AggregationBuilders.avg("agg1").field("age"))
                .get();



       // Get your facet results 分组+

        Terms agg = sr.getAggregations().get("age");
        agg.getBuckets().stream().forEach((a) ->  System.out.println("年龄为"+a.getKey() + "有" + a.getDocCount()));


        Avg agg1 = sr.getAggregations().get("agg1");
        System.out.println(agg1.getValue());


        TopHits topHits = sr.getAggregations().get("top");
        for (SearchHit hit : topHits.getHits().getHits()) {
            System.out.printf(" -> id [%s], _source [%s]", hit.getId(), hit.getSourceAsString());
        }
    }


    /**
     * 插入doc
     * @param client
     * @throws IOException
     */
    public static void insertDoc(TransportClient client) throws IOException {

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("index", "type", "3")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .field("age", 11)
                        .field("env", "test")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("index", "type", "4")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .field("age", 24)
                        .field("env", "test")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.out.println("批量操作失败");
            bulkResponse.forEach(System.out::println);
        }
    }




    /**
     * 简单的搜索方式
     * @param client
     */
    public static void search(TransportClient client){
        SearchResponse response = client.prepareSearch( "index")
                .setTypes( "type")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("env", "test"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(10).to(30))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();


        for (SearchHit hit : response.getHits().getHits()) {
            //Handle the hit...
            System.out.println(hit.getSource().toString());
        }
    }

}
