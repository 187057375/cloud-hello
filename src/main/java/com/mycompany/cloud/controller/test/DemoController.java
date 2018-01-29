package com.mycompany.cloud.controller.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.domain.test.MongoBook;
import com.mycompany.cloud.service.test.MongoBookRepository;
import com.mycompany.cloud.service.test.MongoBookService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/demo")
public class DemoController extends BaseController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private AmqpTemplate rabbitTemplate;
//    @Autowired
//    private EsBookRepository esBookRepository;
    @Autowired
    private MongoBookRepository mongoBookRepository;
    @Autowired
    private MongoBookService mongoBookService;

    @Autowired
    private RestTemplate restTemplate;



    Log LOGGER = LogFactory.getLog(DemoController.class);

    /**
     * 访问chutang-cloud-config配置中心演示
     *
     * @return
     * @author peter 2017年8月31日
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "read remote prop hello=";
    }

    /**
     * chutang orm框架访问数据库演示
     *
     * @return
     * @author peter 2017年8月31日
     */
    @RequestMapping(value = "/db", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> db() {
        Map<String, Object> result = new HashMap<>();
        try {
            LOGGER.info("this is info");
            LOGGER.debug("this is debug");
            LOGGER.error("this is error");
            result.put("result", true);
            String a = null;
            //a.split(a, 1);
            result.put("data", "");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            LOGGER.error("eee", e);
            e.printStackTrace();
        }
        return result;
    }
    //rabbitMq发送消息-demo

    /**
     * chutang mq演示：发送消息
     *
     * @return
     * @author peter 2017年10月10日
     */
    @RequestMapping(value = "/msgQueue", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> mq() {
        Map<String, Object> result = new HashMap<>();
        try {
            String sendMsg = "hello: " + new Date().toLocaleString();
            rabbitTemplate.convertAndSend("msgQueue", sendMsg);
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/agentUserQueue", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> mqobj() {
        Map<String, Object> result = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend("agentUserQueue", "hello");
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    //定时任务-demo
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        System.out.println("现在时间：" + dateFormat.format(new Date()));
//    }


    //es使用-demo
/*
    @RequestMapping(value = "/es", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> es() {
        Map<String, Object> result = new HashMap<>();
        try {
            EsBook book = new EsBook("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
            EsBook testBook = esBookRepository.save(book);
            System.out.println(testBook.getTitle());
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/esfind", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> esfind() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EsBook> byTitle = esBookRepository.findByTitle("Elasticsearch Basics");
            result.put("result", true);
            result.put("msg", byTitle.get(0).getTitle());
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
*/

    ////mongo使用-demo
    @RequestMapping(value = "/addMongoBook", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public MongoBook addMongoBook(@RequestParam("firstname") String firstname,
                                  @RequestParam("secondname") String secondname) {
        //
        DBObject documents = new BasicDBObject("name", "张三")
                .append("age", 45)
                .append("sex", "男")
                .append("address", new BasicDBObject("postCode", 100000).append("street", "深南大道888号").append("city", "深圳"));


        //对象方式
        MongoBook customer = new MongoBook();
        customer.setFirstname(firstname);
        customer.setSecondname(secondname);
        return mongoBookRepository.save(customer);

    }

    @RequestMapping(value = "/getAllMongoBook", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> getAllMongoBook() {
        return mongoBookRepository.findAll();
    }

    @RequestMapping(value = "/getMongoBookByFirstname", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public MongoBook getMongoBookByFirstname(@RequestParam("firstname") String firstname) {
        return mongoBookRepository.findByFirstname(firstname);
    }

    @RequestMapping(value = "/getMongoBookBySecondname", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> getMongoBookBySecondname(@RequestParam("secondname") String secondname) {
        return mongoBookRepository.findBySecondname(secondname);
    }

    @RequestMapping(value = "/deleteMongoBookById", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public boolean deleteMongoBookById(@RequestParam("cid") String cid) {
        mongoBookRepository.delete(cid);
        return true;
    }

    @RequestMapping(value = "/findByQuery", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> findByQuery(@RequestParam("firstname") String firstname) {
        return mongoBookRepository.findByQuery(firstname);
    }

    @RequestMapping(value = "/findByTemplate", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> findByTemplate(@RequestParam("firstname") String firstname) {
        return mongoBookService.findByTemplate(firstname);
    }

    @RequestMapping(value = "/remoteObj", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object remote() throws Exception {
        List<Map> test = restTemplate.getForObject("http://chutang-cloud-building/test/demo/remote", List.class);
        for (Map news : test) {
            System.out.println(news.get("id"));
        }
        return test;
    }



    @RequestMapping(value = "/logtest", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> logtest() {
        Map<String, Object> result = new HashMap<>();
        try {
            LOGGER.info("this is info");
            LOGGER.debug("this is debug");
            LOGGER.error("this is error");
            result.put("result", true);
            String a = null;
            a.split(a, 1);
            result.put("data", "");

        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            LOGGER.error("logtest错误", e);
            e.printStackTrace();
        }
        return result;
    }


}
