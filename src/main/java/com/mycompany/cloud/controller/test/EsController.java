package com.mycompany.cloud.controller.test;

import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.domain.test.EsBook;
import com.mycompany.cloud.service.test.EsBookRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/es")
public class EsController extends BaseController {

    /*ElasticsearchRepository spring.data.elasticsearch查询*/
    @Autowired
    private EsBookRepository esBookRepository;

    /*原生 es api*/
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    Log LOGGER = LogFactory.getLog(EsController.class);

    //es使用-demo
    @RequestMapping(value = "/save" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> save() {
        Map<String, Object> result = new HashMap<>();
        try {
            for (int i=0;i<10;i++){
                EsBook book =new EsBook("100"+i, "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
                EsBook testBook = esBookRepository.save(book);
                System.out.println(testBook.getTitle());
            }
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

    @RequestMapping(value = "/find" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> find() {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<EsBook> esBookPage = esBookRepository.findAll(new PageRequest(1, 4));


            Iterable<EsBook> bookIterable = esBookRepository.findAll();
            //result.put("data", bookIterable);
            Iterator hhh =  bookIterable.iterator();
            while (hhh.hasNext()){
                EsBook book = (EsBook) hhh.next();
                System.out.println(book.getId());
            }
            List<EsBook> byTitleList = esBookRepository.findByTitle("Elasticsearch Basics");
            result.put("result", true);
            //result.put("msg",byTitleList);
            result.put("page", esBookPage);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

    @RequestMapping(value = "/findByApi" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> findByApi() {
        Map<String, Object> result = new HashMap<>();
        try {

//            SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                    .withQuery(matchAllQuery())
//                    .withFilter(boolFilter().must(termFilter("id", documentId)))
//                    .build();
//
//            Page<EsBook> sampleEntities =  elasticsearchTemplate.queryForPage(searchQuery,EsBook.class);
//
//
//
//            TransportClient client = TransportClient.builder().build().addTransportAddress(
//                    new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));


            result.put("result", true);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

}
