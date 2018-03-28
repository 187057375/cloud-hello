package org.zjt;

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
 * Created by zhangjuntao on 2017/4/10.
 */
public class TestTermLevelQueriesApi {

    public static void main( String[] args )throws Exception {

        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                /*.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));*/

        //client.listedNodes().stream().forEach(System.out::println);


        searchTypeAndIdsTermDoc(client);

        client.close();
        System.out.println( "Hello World!" );
    }




    /**
     * searchTermDoc：得到所有的doc文档
     * @param client
     */
    public static void searchTermDoc(TransportClient client){
        /**
         * termsQuery：field包括"out", "another"
         */
        QueryBuilder qb = termsQuery("message", "out", "another");
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
     * rangeQuery：  age 属于 [0,30)
     * @param client
     */
    public static void searchRangeTermDoc(TransportClient client){

        /**
         * rangeQuery：  age 属于 [0,30)
         */
        QueryBuilder qb = rangeQuery("age")
                .from(0).to(30)
                .includeLower(true)
                .includeUpper(false);

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
     * rangeQuery：  10=< age < 20
     * @param client
     */
    public static void searchRangeTermDoc2(TransportClient client){

        /**
         * rangeQuery：  10=< age < 30
         */
        QueryBuilder qb = rangeQuery("age")
                .gte("10")
                .lt("30");

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
     * existsQuery ： 得到包含 filed的doc
     * @param client
     */
    public static void searchExistsTermDoc(TransportClient client){

        /**
         * existsQuery ： 得到包含 filed的doc
         */
        QueryBuilder qb = existsQuery("user");

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
     * prefixQuery（field ,prefix）： field属性前缀prefix查询
     * @param client
     */
    public static void searchPrefixTermDoc(TransportClient client){

        /**
         * prefixQuery（field ,prefix） ： 查询doc field 前缀是prefix
         */
        QueryBuilder qb = prefixQuery(
                "message",
                "tryi"
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
     * 通配符匹配
     * wildcardQuery（field ,prefix）： field属性前缀prefix查询
     * @param client
     */
    public static void searchwildcardTermDoc(TransportClient client){


        QueryBuilder qb = wildcardQuery("user", "k?mc*");

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
     * 正则匹配
     * regexpQuery（field ,prefix）： 查询doc field 符合通配符的doc
     * @param client
     */
    public static void searchRegexpTermDoc(TransportClient client){

        /**
         * regexpQuery（field ,prefix） ： 查询doc field 符合通配符的doc
         */
        QueryBuilder qb = regexpQuery("user", "k*");

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
     * 模糊查询
     * fuzzyQuery（field ,prefix）：
     * @param client
     */
    public static void searchFuzzyTermDoc(TransportClient client){

        /**
         * regexpQuery（field ,prefix） ： 查询doc field 符合通配符的doc
         */
        QueryBuilder qb = fuzzyQuery("user", "kimzhy");

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
     * 得到某个type中的所有doc
     * typeQuery（field ,prefix）：
     * @param client
     */
    public static void searchTypeTermDoc(TransportClient client){

        /**
         * typeQuery（field ） ： 得到某个type中的所有doc
         */
        QueryBuilder qb = typeQuery("type");

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
     *  得到多个type中的所有ids
     * idsQuery（type ,type).addIds(ids)： 查询多个type下的多ids查询
     * @param client
     */
    public static void searchTypeAndIdsTermDoc(TransportClient client){

        /**
         * typeQuery（field ） ： 得到某个type中的所有doc
         */
        QueryBuilder qb = idsQuery("type", "type2")
                .addIds("1", "2", "4");

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
