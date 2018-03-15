package com.mycompany.cloud.controller.test.zookeeper.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/13
 *          Copyright 2018 by PreTang
 */
public class DistributedBarrierExample {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();

        //创建屏障类 不同JVM需要使用相同的目录 即/DistributedBarrier
        final DistributedBarrier barrier = new DistributedBarrier(client, "/DistributedBarrier");
        //创建屏障节点
        barrier.setBarrier();

        //启动一个线程，5000毫秒后移除屏障
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    barrier.removeBarrier();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //等待屏障移除
        barrier.waitOnBarrier();
        System.out.println("======屏障已经移除======");

        Thread.sleep(30000);
        client.close();
    }
}