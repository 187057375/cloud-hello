package com.mycompany.cloud.controller.test;

import com.alibaba.fastjson.JSON;
import com.mycompany.cloud.domain.test.NewsMb;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author peter  org.springframework.amqp.core.
 * @version V1.0 创建时间：17/10/9
 *          Copyright 2017 by PreTang
 *          接受消息
 */
@Component
public class MqReceiver {

    //--------------------创建队列 demo1--------------------
    @Bean
    public org.springframework.amqp.core.Queue msgQueue() {
        return new Queue("msgQueue");
    }

    @RabbitHandler
    @RabbitListener(queues = "msgQueue")
    public void msgQueueReceiver (String msg )
    {
        System.out.println("demo msgQueue Receiver  : " +msg);
    }

    //--------------------创建队列 demo1--------------------



    //--------------------创建队列TopicExchange demo2--------------------
    @RabbitHandler
    @RabbitListener(queues = "newsQueue")
    public void newsQueueReceiver2(NewsMb news)
    {
        System.out.println("demo newsQueue Receiver2  : ");
        System.out.println(JSON.toJSON(news));
    }

    @RabbitHandler
    @RabbitListener(queues = "newsQueue")
    public void newsQueueReceiver1(NewsMb news)
    {
        System.out.println("demo newsQueue Receiver1  : ");
        System.out.println(JSON.toJSON(news));
    }

    @RabbitHandler
    @RabbitListener(queues = "newsQueue.hello")
    public void newsQueueHelloReceiver2(NewsMb news)
    {
        System.out.println("demo newsQueue.hello Receiver2  : ");
        System.out.println(JSON.toJSON(news));
    }

    //创建交换机
    @Bean
    public TopicExchange myTopicExchange(){
        return new TopicExchange("myTopicExchange");
    }
    //创建队列
    @Bean
    public org.springframework.amqp.core.Queue newsQueue() {
        return new Queue("newsQueue");
    }
    @Bean
    public org.springframework.amqp.core.Queue newsQueueHello() {
        return new Queue("newsQueue.hello");
    }
    //绑定队列
    @Bean
    Binding bindingTopicExchange1(@Qualifier("newsQueue")Queue newsQueue,TopicExchange myTopicExchange){
        return BindingBuilder.bind(newsQueue).to(myTopicExchange).with("newsQueue");
    }
    @Bean
    Binding bindingTopicExchange2(@Qualifier("newsQueueHello")Queue newsQueueHello,TopicExchange myTopicExchange){
        return BindingBuilder.bind(newsQueueHello).to(myTopicExchange).with("newsQueue.hello");
    }
    //--------------------创建队列 demo2--------------------



    //--------------------创建FanoutExchange广播模式或者订阅模式 demo3--------------------
    //创建交换机
    @Bean
    FanoutExchange myFanoutExchange() {
        return new FanoutExchange("myFanoutExchange");
    }
    //创建队列
    @Bean
    public org.springframework.amqp.core.Queue newsFanoutQueueA() {
        return new Queue("newsFanoutQueue.a");
    }
    @Bean
    public org.springframework.amqp.core.Queue newsFanoutQueueB() {
        return new Queue("newsFanoutQueue.b");
    }

    //绑定队列
    @Bean
    Binding bindingFanoutExchange1(@Qualifier("newsFanoutQueueA")Queue newsFanoutQueueA,FanoutExchange myFanoutExchange){
        return BindingBuilder.bind(newsFanoutQueueA).to(myFanoutExchange);
    }
    //绑定队列
    @Bean
    Binding bindingFanoutExchange2(@Qualifier("newsFanoutQueueB")Queue newsFanoutQueueB,FanoutExchange myFanoutExchange){
        return BindingBuilder.bind(newsFanoutQueueB).to(myFanoutExchange);
    }

    @RabbitHandler
    @RabbitListener(queues = "newsFanoutQueue.a")
    public void processNewsFanoutQueue(NewsMb news)
    {
        System.out.println("newsFanoutQueue.a Receiver  : ");
        System.out.println(JSON.toJSON(news));
    }
    //--------------------创建广播模式或者订阅模式 demo3--------------------


    //--------------------创建DirectExchange demo4--------------------
    //创建交换机
    @Bean
    public DirectExchange myDirectExchange(){
        return new DirectExchange("myDirectExchange");
    }
    //创建队列
    @Bean
    public org.springframework.amqp.core.Queue myDirectNewsQueue() {
        return new Queue("myDirectNewsQueue");
    }
    //绑定队列
    @Bean
    Binding bindingDirectExchange(@Qualifier("myDirectNewsQueue")Queue myDirectNewsQueue,DirectExchange myDirectExchange){
        return BindingBuilder.bind(myDirectNewsQueue).to(myDirectExchange).with("aaa");
    }
    //接受消息方式1
//    @RabbitHandler
//    @RabbitListener(queues = "myDirectNewsQueue")
//    public void processMyDirectNewsQueue(News news)
//    {
//        System.out.println("myDirectNewsQueue Receiver  : ");
//        System.out.println(JSON.toJSON(news));
//    }
    //接受消息方式2
    @RabbitHandler
    @RabbitListener( bindings = @QueueBinding(
            value =@org.springframework.amqp.rabbit.annotation.Queue(value = "myDirectNewsQueue" , durable = "true"),
            exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value = "myDirectExchange" , type = "direct" , durable = "true") ,
            key = "aaa")
    )
    public void receiveMessage(NewsMb news){
        System.out.println("myDirectNewsQueue Receiver @annotation : ");
        System.out.println(JSON.toJSON(news));
    }
    //--------------------创建队列 demo4--------------------




}