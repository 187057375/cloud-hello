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
public class TestCompoundQueriesApi {

    public static void main( String[] args )throws Exception {

        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                /*.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));*/

        //client.listedNodes().stream().forEach(System.out::println);


        /**
         * 1、查询，并返回boost 分数的结果
         */
        QueryBuilder qb = constantScoreQuery(
                termQuery("user","kimchy")
        ).boost(1.320f);
        System.out.println(qb.boost());


        /**
         * 2、boolQuery多条件查询。
         *  must 必须满足
         *  mustNot 必须不是
         *  should  可以不满足
         *  filter  必须满足
         */
        qb = boolQuery()
                .must(termQuery("user", "kimchy"))
                .must(termQuery("env", "test"))
                .mustNot(termQuery("message", "out"))
                .should(termQuery("message", "post-111"))
                .filter(termQuery("message", "another"));

        scoreQuery(client , qb);





        client.close();
        System.out.println( "Hello World!" );
    }




    public static void  scoreQuery(TransportClient client , QueryBuilder qb){

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