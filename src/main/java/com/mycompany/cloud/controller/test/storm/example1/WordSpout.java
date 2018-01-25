package com.mycompany.cloud.controller.test.storm.example1;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @author peter
 * @version V1.0 创建时间：18/1/19
 *          Copyright 2018 by PreTang
 */

public class WordSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private static String[] words = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    public WordSpout() {

    }

    public void nextTuple() {
        String word = words[new Random().nextInt(words.length)]; //随机取 words 字符串中一个词。
        collector.emit(new Values(word));//发射元组到输出收集器

    }

    public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
        this.collector=arg2;//定义数据源输出收集器

    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        arg0.declare(new Fields("word"));//声明输出字段的名称为为 word
    }

}
