package com.mycompany.cloud.controller.test.redis.demo;

import redis.clients.jedis.Jedis;


public class RedisMain {
    public static void main(String[] args) {
        //创建redis客户端
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //运行redis ping命令，正常返回pong
        String response = jedis.ping();
        //打印结果，正常结果为pong
        System.out.println(response);
    }
}