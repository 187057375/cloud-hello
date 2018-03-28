package com.mycompany.cloud.controller.test.es.main;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 *
 * 查询测试 ：
 *  a b -> 包括a 或者 b
 *  +a -b ->包含a 不包含 b
 *
 *  全文查询编辑
 *  高级全文查询通常用于在全文本字段（如电子邮件正文）上运行全文查询。他们了解如何对被查询的字段进行分析，并在执行前将每个字段的 analyzer（或search_analyzer）应用于查询字符串。
 *  该组中的查询是：

 * match 查询
    用于执行全文查询的标准查询，包括模糊匹配和短语或邻近查询。
 * multi_match 查询
    多字段版本的match查询。
 * common_terms 查询
    一个更专业的查询，更多的偏好不常见的单词。
 * query_string 查询
    支持紧凑的Lucene查询字符串语法，允许您在单个查询字符串中指定AND | OR | NOT条件和多字段搜索。仅适用于专家用户。
 * simple_query_string
    一种更简单，更强大的query_string语法版本，适合直接暴露给用户。
 */
public class TestQueryDSLApi {


    public static void main( String[] args )throws Exception {

        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                /*.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));*/

        //client.listedNodes().stream().forEach(System.out::println);



        searchStringAllDoc(client);

        client.close();
        System.out.println( "Hello World!" );
    }



    /**
     * matchAllQuery：得到所有的doc文档
     * @param client
     */
    public static void searchAllDoc(TransportClient client){
        QueryBuilder qb = matchAllQuery();

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }


    /**
     * 查询匹配文档（a b 包含a或者b的文档）
     * @param client
     */
    public static void searchMatchDoc(TransportClient client){

        /**
         * 查询doc message中包括post和out的文档
         */
        QueryBuilder qb = matchQuery(
                "message",
                "post out"
        );

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }




    /**
     * 查询多个字段field 包含内容字段
     * @param client
     */
    public static void searchMultiMatchDoc(TransportClient client){

        /**
         * 查询doc message中包括post和out的文档
         */
        QueryBuilder qb = multiMatchQuery(
                "kimchy elasticsearch",     //field名包含 kimchy 或者 elasticsearch
                "user", "message"    //field名
        );

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }




    public static void searchCommonMatchDoc(TransportClient client){

        /**
         * 查询doc message中包括post和out的文档
         */
        QueryBuilder qb = commonTermsQuery("user","kimchy");

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }


    /**
     * 查询文档doc包含某个字符串,有异常是不会返回信息，不能返回全部的信息
     * @param client
     */
    public static void searchStringDoc(TransportClient client){
        /**
         * 查询doc包含+kimchy 不包含-elasticsearch
         */
        QueryBuilder qb = queryStringQuery("+kimchy -elasticsearch");

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }




    /**
     * 查询文档doc包含某个字符串，返回整个文档的信息
     * @param client
     */
    public static void searchStringAllDoc(TransportClient client){
        /**
         * 查询doc包含+kimchy 不包含-elasticsearch
         */
        QueryBuilder qb = simpleQueryStringQuery("+kimchy -elasticsearch");

        SearchResponse scrollResp = client.prepareSearch("index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2).get(); //max of 100 hits will be returned for each scroll

        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getSource().toString());
            }
            scrollResp = client
                    .prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
    }




}
