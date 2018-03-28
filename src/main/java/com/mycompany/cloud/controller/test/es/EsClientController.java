package com.mycompany.cloud.controller.test.es;

import com.mycompany.cloud.controller.BaseController;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/es/esclient")
public class EsClientController extends BaseController {

    @Autowired
    private TransportClient client;

    @GetMapping("/get/book/novel")
    @ResponseBody
    public ResponseEntity get(@RequestParam(value = "id", defaultValue = "") String id) {
        if (id.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        GetResponse result = client.prepareGet("book", "novel", id).get();
        if (!result.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/put/book/novel")
    @ResponseBody
    public ResponseEntity add(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("word_count") int wordCount,
            @RequestParam("publish_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    Date publishDate
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(publishDate));
        try {
            XContentBuilder contentBuilder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("title", title)
                            .field("author", author)
                            .field("word_count", wordCount)
                            .field("publish_date", format.format(publishDate))
                            .endObject();
            System.out.println(contentBuilder.toString());
            IndexResponse result = client.prepareIndex("book", "novel").setSource(contentBuilder).get();

            return new ResponseEntity(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/delete/book/novel")
    @ResponseBody
    public ResponseEntity delete(@RequestParam("id") String id) {

        DeleteResponse result = this.client.prepareDelete("book", "novel", id).get();
        return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
    }

    @PutMapping("/update/book/novel")
    @ResponseBody
    public ResponseEntity update(
            @RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "word_count", required = false) Integer wordCount,
            @RequestParam(value = "publish_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    Date publishDate
    ) {
        UpdateRequest updateRequest = new UpdateRequest("book", "novel", id);
        try {
            XContentBuilder contentBuilder =
                    XContentFactory.jsonBuilder().startObject();
            if (title != null)
                contentBuilder.field("title", title);
            if (author != null)
                contentBuilder.field("author", author);
            if (wordCount != null)
                contentBuilder.field("word_count", wordCount);
            if (publishDate != null)
                contentBuilder.field("publish_date",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishDate));
            contentBuilder.endObject();
            updateRequest.doc(contentBuilder);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            UpdateResponse result = this.client.update(updateRequest).get();
            return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }//update


    @PostMapping("/query/book/novel")
    @ResponseBody
    public ResponseEntity query(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "lt_word_count", required = false) Integer ltWordCount,
            @RequestParam(value = "gt_word_count", required = false, defaultValue = "0")
                    Integer gtWordCount
    ) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (title != null)
            boolQuery.must(QueryBuilders.matchQuery("title", title));
        if (author != null)
            boolQuery.must(QueryBuilders.matchQuery("author", author));

        RangeQueryBuilder rangeQuery =
                QueryBuilders.rangeQuery("word_count")
                        .from(gtWordCount);
        if (ltWordCount != null)
            rangeQuery.to(ltWordCount);
        boolQuery.filter(rangeQuery);

        SearchRequestBuilder searchRequestBuilder =
                this.client.prepareSearch("book")
                        .setTypes("novel")
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(boolQuery)
                        .setFrom(0)
                        .setSize(10);
        System.out.println(searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();

        List<Map<String, Object>> result = new ArrayList<>();

        for (SearchHit searchHit : searchResponse.getHits()) {
            result.add(searchHit.getSource());
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }


    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
