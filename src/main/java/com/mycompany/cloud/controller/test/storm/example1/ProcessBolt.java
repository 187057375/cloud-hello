package com.mycompany.cloud.controller.test.storm.example1;

/**
 * @author peter
 * @version V1.0 创建时间：18/1/19
 * Copyright 2018 by PreTang
 */

import com.alibaba.fastjson.JSON;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessBolt extends BaseRichBolt {

    public OutputCollector _collector;
    public ProcessBolt() {

    }


    public void execute(Tuple arg0) {
        //此处直接对接受到的元组进行处理，然后输出到控制台，这里没有将处理后的数据再送到输出收集器中。
        String word = (String) arg0.getValue(0); //取得元组的数据
        String out = "Hello ：" + word + "!";
        System.err.println(out); //输出到控制台，使用 err.println 会显示红色，所以这里使用 err

        try {
            List myList =new ArrayList();
            System.out.println(JSON.toJSONString(myList));
        } catch (Exception e) {
            e.printStackTrace();
        }


        _collector.emit(arg0, new Values(word + "!!!"));
        _collector.ack(arg0);
    }

    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
        _collector = arg2;
    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {

    }

}
