package com.mycompany.cloud.controller.test.redis;

import com.mycompany.cloud.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 演示redis
 * @author peter
 * @version V1.0 创建时间：17/8/31
 */
@RestController
@RequestMapping("/test/redis/testRedis")
public class TestRedisController extends BaseController {


    private Log LOGGER = LogFactory.getLog(TestRedisController.class);

    @Autowired
    private RedisUtil redisUtil;
    @ResponseBody
    @RequestMapping(value = "/set")
    public void set() {
        System.out.println("j================"+redisUtil.set("cloud-hello","cloud-hello-vvvvvv"));
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public void get() {

        System.out.println("p================"+redisUtil.get("cloud-hello"));
    }




}
