package com.mycompany.cloud.controller.test.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author peter
 * @version V1.0 创建时间：18/1/17
 *          Copyright 2018 by PreTang
 */
public class MapReduceWordCount {
    public static class MyMapper extends Mapper<Object, Text, Text, IntWritable>{
        private final static IntWritable one = new IntWritable(1);
        private Text event = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println("====="+value.toString());
            int idx = value.toString().indexOf(" ");
            if (idx > 0) {
                String e = value.toString().substring(0, idx);
                event.set(e);
                context.write(event, one);
            }
        }
    }

    public static class MyReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                System.out.println("++++++"+val.get());
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:8020");

        Job job = Job.getInstance(conf, "event count");
        job.setJarByClass(MapReduceWordCount.class);
        job.setMapperClass(MyMapper.class);
        job.setCombinerClass(MyReducer.class);
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path("/user/peter/test.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/user/peter/test_out2"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);


        //查看结果 hdfs dfs -cat /user/peter/test_out/part-r-00000
    }


}