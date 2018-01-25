package com.mycompany.cloud.controller.test.storm.example3kafka;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author peter
 * @version V1.0 创建时间：18/1/19
 *          Copyright 2018 by PreTang
 */
public class BoltPrint extends BaseRichBolt{

    private static Logger LOG = LoggerFactory.getLogger(BoltPrint.class);
    private OutputCollector _collector;

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    public void execute(Tuple tuple) {
        System.out.println("----执行boltprint----："+tuple.getString(0));
        LOG.info("----执行boltprint----："+tuple.getString(0));
        _collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
