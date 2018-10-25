package com.mycompany.cloud.controller.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 *
 * org.apache.kafka » kafka 1.0.0
 * @author peter
 * @version V1.0 创建时间：18/1/22
 * Copyright 2018 by PreTang
 */

public class MyKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.129:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");




        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i = 0; i < 100; i++) {
            System.out.println("----send:" + i + "");
            producer.send(new ProducerRecord<>("sys_log_send", Integer.toString(i), Integer.toString(i)));
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }
}