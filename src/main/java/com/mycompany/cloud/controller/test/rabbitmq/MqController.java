package com.mycompany.cloud.controller.test.rabbitmq;

import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.domain.test.NewsMb;
import com.mycompany.cloud.service.test.MongoBookRepository;
import com.mycompany.cloud.service.test.MongoBookService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/mq")
public class MqController extends BaseController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private MongoBookRepository mongoBookRepository;
    @Autowired
    private MongoBookService mongoBookService;

    @Autowired
    private RestTemplate restTemplate;




    Log LOGGER = LogFactory.getLog(MqController.class);

    //rabbitMq发送消息-demo
    /**
     * chutang mq演示：发送消息
     * @return
     * @author peter 2017年10月10日
     */
    @RequestMapping(value = "/msgQueue" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
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
        return  result;
    }
    @RequestMapping(value = "/newsQueue" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> mqobj() {
        Map<String, Object> result = new HashMap<>();
        try{

            //rabbitTemplate.convertAndSend("newsQueue", news);


            //rabbitTemplate.convertAndSend("myExchange","newsQueue",news);
            //rabbitTemplate.convertAndSend("myExchange","newsQueue.hello",news);

            //rabbitTemplate.convertAndSend("fanoutExchange","random",news);

                rabbitTemplate.convertAndSend("myDirectExchange","aaa",new NewsMb());
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }
    //定时任务-demo
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        System.out.println("现在时间：" + dateFormat.format(new Date()));
//    }


}
