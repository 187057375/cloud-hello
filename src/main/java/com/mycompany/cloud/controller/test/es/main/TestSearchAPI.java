package com.mycompany.cloud.controller.test.es.main;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
    • Elasticsearch的查询 • es的搜索类型有4种 
    – query and fetch(速度最快)(返回N倍数据量) 
    – query then fetch（默认的搜索方式） 
    – DFS query and fetch(可以更精确控制搜索打分和排名。) 
    – DFS query then fetch • DFS解释：见备注 • 
    总结一下，
        从性能考虑QUERY_AND_FETCH是最快的，
         DFS_QUERY_THEN_FETCH是最慢的。
         从搜索的准确度来说，DFS要比非DFS的 准确度更高
 * Created by Administrator on 2017/4/9.
 */
public class TestSearchAPI {
    public static void main(String [] ARGS)throws Exception{

      /* .add("database", "user", "1")
                .add("index", "type", "1","2")*/
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("centos"), 9300));



        searchHighLight(client);

        client.close();
        System.out.println( "close client!" );
    }


    /**
     * 简单的搜索方式
     * @param client
     */
    public static void search(TransportClient client){
        SearchResponse response = client.prepareSearch( "index")
                .setTypes( "type1","type2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("env", "test"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(10).to(18))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();



        for (SearchHit hit : response.getHits().getHits()) {
            //Handle the hit...
            System.out.println(hit.getSource().toString());
        }
    }


    /**
     * 高亮显示的搜索方式
     * @param client
     */
    public static void searchHighLight(TransportClient client){
        /**添加高亮显示的字段、该字段的前后缀*/
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("message").field("user").preTags("   高亮[").postTags("]高亮   ");
        SearchResponse response = client.prepareSearch( "database")
                .setTypes( "user")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("user", "kimchy"))                 // Query
                .setFrom(0).setSize(60).setExplain(true)
                .highlighter(highlightBuilder)
                .get();

        /**高亮显示*/

        for (SearchHit hit : response.getHits().getHits()) {

            /** result: {postDate=2017-04-18T06:03:29.237Z, message=trying out Elasticsearch, user=kimchy} */
            System.out.println(hit.getSource().toString());

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("user");
            Map<String, Object> source = hit.getSource();
            if(highlightField!=null){
                Text[] fragments = highlightField.fragments();
                String name = "";
                for (Text text : fragments)
                    name+=text;
                source.put("title", name);
            }

            /** result: {postDate=2017-04-18T06:03:29.237Z, message=trying out Elasticsearch, title=   高亮[kimchy]高亮   , user=kimchy}  */
            System.out.println(source.toString());
        }
    }





    /**
     * 分页查找： SearchResponse 实现的term的分页查.
     * @param client
     *
     * PS: prepareSearchScroll(scrollResp.getScrollId()) //设置每次查找得到的ScrollId，来分页
     */
    public static void searchByTerm(TransportClient client){
        QueryBuilder qb = termQuery("env", "test");

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
     * mutilSearch 一次实现多个查找动作（多个查询没有相互关系）
     * @param client
     *
     * PS: queryStringQuery 查询出内容包含某字符串的文档
     */
    public static void mutilSearch(TransportClient client){
        SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);   // 查询出一个内容包含Elasticsearch文档
        SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("env", "test")).setSize(1);          //查询出一个属性和值对应的文档

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        // You will get all individual responses from MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            for (SearchHit hit : response.getHits().getHits())
                System.out.println(hit.getSource().toString());
            nbHits += response.getHits().getTotalHits();
        }
    }









}
