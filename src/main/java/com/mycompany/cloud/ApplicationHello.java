package com.mycompany.cloud;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


/**
 * @author peter
 * @version V1.0 创建时间：17/09/01
 *          Copyright 2017 by PreTang
 */
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
public class ApplicationHello {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        restTemplate.getMessageConverters().add(fastConverter);
        return  restTemplate;
    }

    /**
     * rabbitmq监听方法参数转换hack
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
    /**
     * rabbitmq监听方法参数转换hack
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
    public static void main(String[] args) {
        System.out.println("hello-----");
        new SpringApplicationBuilder(ApplicationHello.class).web(true).run(args);
    }

}