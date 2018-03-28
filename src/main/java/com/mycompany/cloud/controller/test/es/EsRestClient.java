package com.mycompany.cloud.controller.test.es;

import com.mycompany.cloud.controller.BaseController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/16
 *          Copyright 2018 by PreTang
 */
@RestController
@RequestMapping("/test/es/esrestclient")
public class EsRestClient extends BaseController {
    @RequestMapping("/home")
    String home() {

        String target = System.getenv("TARGET");
        if (target == null) {
            target = "localhost";
        }

        RestClient client = RestClient.builder(new HttpHost(target, 9200, "http")).build();


        Map<String, String> params = Collections.emptyMap();
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Random rand = new Random();
        int n = rand.nextInt(10000) + 1;
        try {
            Response response = client.performRequest("PUT", "/posts/doc/" + n, params, entity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        System.out.println("about to close");
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Hello World!";
    }

}
