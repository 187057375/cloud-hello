package com.mycompany.cloud.controller.test.kafka;

/**
 * 0.11
 * @author peter
 * @version V1.0 创建时间：18/1/22
 *          Copyright 2018 by PreTang
 */

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author mengfanzhu
 * @Package bigdata.kafka
 * @Description: kafka 消费者
 * @date 17/3/8 17:21
 */
public class KafkaConsumerExample {

    //config
    public static Properties getConfig()
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", "testGroup");
        props.put("enable.auto.commit", "true");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

    public void consumeMessage()
    {
        // launch 3 threads to consume
        int numConsumers = 1;
        final String topic = "slavetest";
        final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
        final List<KafkaConsumerRunner> consumers = new ArrayList<KafkaConsumerRunner>();
        for (int i = 0; i < numConsumers; i++) {
            KafkaConsumerRunner consumer = new KafkaConsumerRunner(topic);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                for (KafkaConsumerRunner consumer : consumers) {
                    consumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Thread to consume kafka data
    public static class KafkaConsumerRunner implements Runnable
    {
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private final KafkaConsumer<String, String> consumer;
        private final String topic;

        public KafkaConsumerRunner(String topic)
        {
            Properties props = getConfig();
            consumer = new KafkaConsumer<String, String>(props);
            this.topic = topic;
        }

        public void handleRecord(ConsumerRecord record)
        {
            System.out.println("name: " + Thread.currentThread().getName() + " ; topic: " + record.topic() + " ; offset" + record.offset() + " ; key: " + record.key() + " ; value: " + record.value());
        }

        public void run()
        {
            try {
                // subscribe 订阅｀topic
                consumer.subscribe(Arrays.asList(topic));
                while (!closed.get()) {
                    //read data
                    ConsumerRecords<String, String> records = consumer.poll(10000);
                    // Handle new records
                    for (ConsumerRecord<String, String> record : records) {
                        handleRecord(record);
                    }
                }
            }
            catch (WakeupException e) {
                // Ignore exception if closing
                e.printStackTrace();
                if (!closed.get()) {
                    throw e;
                }
            }
            finally {
                consumer.close();
            }
        }

        // Shutdown hook which can be called from a separate thread
        public void shutdown()
        {
            closed.set(true);
            consumer.wakeup();
        }
    }

    public static void main(String[] args)
    {
        KafkaConsumerExample example = new KafkaConsumerExample();
        example.consumeMessage();
    }
}
