package com.mycompany.cloud.controller.test.storm.example1;

/**
 * @author peter
 * @version V1.0 创建时间：18/1/19
 *          Copyright 2018 by PreTang
 */

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class WordStorm {


    public static void main(String[] args)  throws Exception {
        System.out.println("start..........");
        //定义 TopologyBuilder
        TopologyBuilder builder=new TopologyBuilder();
        //定义 Spout
        builder.setSpout("Spout_ID", new WordSpout(),1);
        //定义 Bolt
        builder.setBolt("Bolt_ID", new ProcessBolt()).localOrShuffleGrouping("Spout_ID");
        //下面开始定义运行模式
        final Config config=new Config();
        config.setDebug(false);

        if (args != null && args.length > 0) {
            //集群运行模式
            config.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createTopology());
        }else {
            //使用本地模式运行
            final LocalCluster localCluster=new LocalCluster();

            localCluster.submitTopology(WordStorm.class.getSimpleName(), config, builder.createTopology());
            org.apache.storm.utils.Utils.sleep(90000);

            localCluster.killTopology(WordStorm.class.getSimpleName());
            localCluster.shutdown();
        }


    }

}
