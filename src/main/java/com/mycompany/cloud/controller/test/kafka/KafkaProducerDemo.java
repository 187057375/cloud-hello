package com.mycompany.cloud.controller.test.kafka;

import kafka.producer.KeyedMessage;
import kafka.producer.Producer;
import kafka.producer.ProducerConfig;
import scala.collection.Seq;

import java.util.Properties;

/**
 * <=0.8
 * @author peter
 * @version V1.0 创建时间：18/1/22
 *          Copyright 2018 by PreTang
 */
public class KafkaProducerDemo {

    private  final Producer<String,String> producer;

    public final static String TOPIC="TEST-TOPIC";

    private KafkaProducerDemo(){
        Properties props=new Properties();
        props.put("metadata.broker.list","192.168.200.129:9092");
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("key.serializer.class","kafka.serializer.StringEncoder");
        props.put("request.required.acks","-1");
        producer=new Producer<String, String>(new ProducerConfig(props)) ;
    }


    void produce(){
        int messageNo=1000;
        final int COUNT=10000;
        while(messageNo<COUNT){
            String key=String.valueOf(messageNo);
            String data="hello kafka message"+key;
            producer.send((Seq<KeyedMessage<String, String>>) new KeyedMessage<String,String>(TOPIC,key,data));
            System.out.println(data);
            messageNo++;
        }
    }

    public  static  void main(String[] args){
        new KafkaProducerDemo().produce();
    }

}


