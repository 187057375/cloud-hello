package com.mycompany.cloud.controller.test.es.main;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/16
 * Copyright 2018 by PreTang
 */

import com.mycompany.cloud.domain.test.NewsMb;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESTransportClient {
    static TransportClient client;

    public static void init() throws UnknownHostException {
        Settings esSettings = Settings.builder()
                .put("transport.type", "netty3")
                .put("http.type", "netty3")
                .put("cluster.name", "elasticsearch-hhee") //设置ES实例的名称
                //.put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .build();
        client = new PreBuiltTransportClient(esSettings);//初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
        //此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public static void main(String[] args) throws Exception {
        init();
//        prepareIndex();
//        insertDocment();
//        prepareGet();
        prepareSearch();
    }


    public static void prepareGet() throws Exception {
        GetResponse response = client.prepareGet("test", "user", "AWIt_EpIlpNVbypZLwCC").execute().actionGet();
        System.out.println("response.getId():" + response.getId());
        System.out.println("response.getSourceAsString():" + response.getSourceAsString());
    }

    public static void prepareSearch() throws Exception {
        //term查询
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("age", 50) ;
        //range查询
        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").gt(50);
        SearchResponse searchResponse = client.prepareSearch("test").setTypes("user")
                .setQuery(rangeQueryBuilder)
                .addSort("age", SortOrder.DESC)
                .setSize(20)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        System.out.println("查到记录数：" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        if (searchHists.length > 0) {
            for (SearchHit hit : searchHists) {
                Map<String, Object> map = hit.getSourceAsMap();
                String name = (String) map.get("name");
                Integer age = (Integer) map.get("age");
                System.out.format("name:%s ,age :%d \n", name, age);
            }
        }
    }


    public static void prepareIndex() throws Exception {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("name", "我的name");
        infoMap.put("title", "这是title");
        infoMap.put("createTime", new Date());
        infoMap.put("count", 1022);
        infoMap.put("xxxx", new NewsMb());
        IndexResponse indexResponse = client.prepareIndex("test", "user", null).setSource(infoMap).execute().actionGet();
        System.out.println("id:" + indexResponse.getId());
    }


    public static void insertDocment() throws Exception {
        IndexResponse response = client.prepareIndex("test", "user", "1").setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
                .get();
        // Index name
        String _index = response.getIndex();
        // Type name// Type name
        String _type = response.getType();
        // Document ID (generated or not)// Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)// Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // status has stored current instance statement.// status has stored current instance statement.
        RestStatus status = response.status();
        // on shutdown

        System.out.printf("_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n", _index, _type, _id, _version, status.toString());
    }

}
