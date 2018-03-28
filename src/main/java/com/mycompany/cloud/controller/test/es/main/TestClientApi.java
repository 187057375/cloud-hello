package com.mycompany.cloud.controller.test.es.main;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Hello world!
 *
 */
public class TestClientApi {
    public static void main( String[] args )throws Exception {

        // on startup

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
                /*.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));*/

        //client.listedNodes().stream().forEach(System.out::println);



        insertDocment(client);
        client.close();
        System.out.println( "Hello World!" );


    }

    /**
     * 插入doc
     * @param client
     * @throws Exception
     */
    public static void insertDocment(TransportClient client)throws Exception{
        IndexResponse response = client.prepareIndex("database", "user", "1")
                .setSource(jsonBuilder()
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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());
    }


    /**
     * 得到doc的信息
     * @param client
     */
    public static void getDocment(TransportClient client){
        GetResponse response = client.prepareGet("index", "type", "1")
                .setOperationThreaded(false)
                .get();

        System.out.println( response.getSourceAsString());

        // Index name
        String _index = response.getIndex();
        // Type name// Type name
        String _type = response.getType();
        // Document ID (generated or not)// Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)// Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // status has stored current instance statement.// status has stored current instance statement.


        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s    \n" ,_index,_type,_id,_version);
    }


    /**
     * 删除doc
     * @param client
     */
    public static void deleteDocment(TransportClient client){
        DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());
    }


    /**
     * 将查询到的结果删除
     * @param client
     */
    public static void deleteByQuery(TransportClient client){
        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        .filter(QueryBuilders.matchQuery("user", "kimchy"))  //查询的属性和值
                        .source("database")   //查询的index
                        .get();


        long deleted = response.getDeleted();

        System.out.printf( "deleted=%s \n" ,deleted);
    }


    /**
     *
     * @param client
     * @throws Exception
     */
    public static void insertOrUppdate(TransportClient client)throws Exception{
        IndexRequest indexRequest = new IndexRequest("index", "type", "2")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Joe Smith")
                        .field("gender", "male")
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "2")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "update-male")
                        .endObject())
                .upsert(indexRequest);
        UpdateResponse response = client.update(updateRequest).get();


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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());

    }


    /**
     * 将现有的属性合并到doc中,增加新属性
     * @param client
     * @throws Exception
     */
    public static void mergeDocment(TransportClient client)throws Exception{
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("age", "merge-24")
                        .endObject());
        UpdateResponse response = client.update(updateRequest).get();



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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());
    }


    /**
     * 通过脚本更新（命令）
     * @param client
     * @throws Exception
     */
    public static void updataByScript(TransportClient client)throws Exception{
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .script(new Script("ctx._source.gender = \"Script-male\""));
        UpdateResponse response = client.update(updateRequest).get();

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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());
    }


    /**
     * 更新api
     * @param client
     * @throws Exception
     */
    public static void update(TransportClient client)throws Exception{
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("index");
        updateRequest.type("type");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .endObject());
        UpdateResponse response = client.update(updateRequest).get();

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

        System.out.printf( "_index=%s  _type=%s    _id=%s  _version=%s   status=%s \n" ,_index,_type,_id,_version,status.toString());
    }


    /**
     * 通过id查出多个doc
     * @param client
     */
    public static void  multiGet(TransportClient client){
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("database", "user", "1")
                .add("index", "type", "1","2")
                .add("another", "type", "foo")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response !=null && response.isExists()) {
                System.out.println( response.getSourceAsString());
            }
        }
    }



    /**
     * 批量操作（插入、删除）
     * @param client
     * @throws Exception
     */
    public static void batchInsert(TransportClient client)throws Exception{
        BulkRequestBuilder bulkRequest = client.prepareBulk();

    // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("index", "type1", "1")
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

        bulkRequest.add(client.prepareIndex("index", "type2", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .field("age", 20)
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





}
