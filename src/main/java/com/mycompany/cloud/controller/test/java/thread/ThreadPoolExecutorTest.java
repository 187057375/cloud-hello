package com.mycompany.cloud.controller.test.java.thread;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/26
 *          Copyright 2018 by PreTang
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor测试类
 * 注意：
 * 1、ThreadPoolExecutor是一个线程池
 * 2、多个任务都可以由该线程池中选出几条线程来执行
 */
public class ThreadPoolExecutorTest {
    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));

    public void executeTask(){
        Task1 task1 = new Task1();//构建任务1
        Task2 task2 = new Task2();//构建任务2
        executor.execute(task1);//执行任务1
        executor.execute(task2);//执行任务2
    }

    /*
     * 基本任务2
     */
    class Task1 implements Runnable{
        public void run() {
            //具体任务的业务
            for(int i=0;i<1000;i++){
                System.out.println("hello xxx!!!");
            }
        }
    }

    /*
     * 基本任务2
     */
    class Task2 implements Runnable{
        public void run() {
            //具体任务的业务
            for(int i=0;i<5;i++){
                System.out.println("hello world2!!!");
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutorTest test = new ThreadPoolExecutorTest();
        test.executeTask();
    }
}