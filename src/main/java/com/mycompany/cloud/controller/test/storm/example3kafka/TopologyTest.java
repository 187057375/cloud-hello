package com.mycompany.cloud.controller.test.storm.example3kafka;

import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Collections;

/**
 * kafka接受消息 执行boltPrint
 * zk3.4 curator2.8.0 版本依赖
 * @author peter
 * @version V1.0 创建时间：18/1/22
 *          Copyright 2018 by PreTang
 */
public class TopologyTest {


    public static void main(String[] args) throws Exception {
        try {
            //实例化topologyBuilder类。
            TopologyBuilder topologyBuilder = new TopologyBuilder();

            //-------配置kafka - storm
            //设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。
            BrokerHosts brokerHosts = new ZkHosts("127.0.0.1:2181");
            //配置Kafka订阅的Topic，以及zookeeper中数据节点目录和名字
            SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, "slavetest", "/storm", "s32");
            spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
            spoutConfig.zkServers = Collections.singletonList("127.0.0.1");
            spoutConfig.zkPort = Integer.parseInt("2181");
            //从Kafka最新输出日志读取
            spoutConfig.startOffsetTime = OffsetRequest.LatestTime();
            KafkaSpout receiver = new KafkaSpout(spoutConfig);
            //-------配置kafka - storm


            topologyBuilder.setSpout("kafka-spout", receiver, 1).setNumTasks(2);
            topologyBuilder.setBolt("alarm-bolt", new BoltPrint(), 1).setNumTasks(2).shuffleGrouping("kafka-spout");
            Config config = new Config();
            config.setDebug(false);
            config.setNumWorkers(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("kafka-spout", config, topologyBuilder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
