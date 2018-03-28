package com.mycompany.cloud.controller.test.distributedlock.demo1.redis;

/**
 * Created by liuyang on 2017/4/20.
 */
public class ThreadA extends Thread {
    private Service service;

    public ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}
