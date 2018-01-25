package com.mycompany.cloud.controller.test;

import com.mycompany.cloud.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * kafka
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/kafka")
//@EnableBinding(Source.class)
public class KafkaController extends BaseController {



    @Autowired
    private KafkaTemplate kafkaTemplate;



    Log LOGGER = LogFactory.getLog(KafkaController.class);

    @RequestMapping(value = "/send" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> send() {
        Map<String, Object> result = new HashMap<>();
        try {
            String sendMsg = "hello: " + new Date().toLocaleString();
            kafkaTemplate.send("test1",sendMsg);
            result.put("result", true);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

    @KafkaListener(topics = "test1")
    public void processMessage(String content) {
        System.out.println(content);
    }




/*    @Autowired
    private SendService service;

    @RequestMapping(value = "/send/{msg}", method = RequestMethod.GET)
    public void send(@PathVariable("msg") String msg){
        try {
            service.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
