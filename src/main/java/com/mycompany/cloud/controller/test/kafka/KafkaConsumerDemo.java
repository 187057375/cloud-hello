package com.mycompany.cloud.controller.test.kafka;

/**
 * 0.8
 * @author peter
 * @version V1.0 创建时间：18/1/22
 *          Copyright 2018 by PreTang
 */
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <=0.8
 * Created by Germmy on 2016/7/10.
 */
public class KafkaConsumerDemo {


    private  final ConsumerConnector consumer;

    public final static String TOPIC="TEST-TOPIC";

    private KafkaConsumerDemo(){
        Properties props=new Properties();
        props.put("zookeeper.connect","127.0.0.1:2181");
        props.put("group.id","jd-group");//消费组是什么概念？

        props.put("zookeeper.session.timeout.ms","60000");
        props.put("zookeeper.sync.time.ms","200");
        props.put("auto.commit.interval.ms","1000");
        props.put("auto.offset.reset","smallest");

        props.put("serializer.class","kafka.serializer.StringEncoder");

        ConsumerConfig config=new ConsumerConfig(props);

        consumer=kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }


    void consume(){
        Map<String,Integer> topicCountMap=new HashMap<String, Integer>();
        topicCountMap.put(KafkaConsumerDemo.TOPIC,new Integer(1));
        StringDecoder keyDecoder=new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder=new StringDecoder(new VerifiableProperties());

        Map<String,List<KafkaStream<String,String>>> consumerMap=
                consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);

        KafkaStream<String,String> stream=consumerMap.get(KafkaConsumerDemo.TOPIC).get(0);
        ConsumerIterator<String,String> it=stream.iterator();
        while(it.hasNext()){
            System.out.println(it.next().message());
        }
    }


    public  static  void main(String[] args){
        new KafkaConsumerDemo().consume();
    }

}
