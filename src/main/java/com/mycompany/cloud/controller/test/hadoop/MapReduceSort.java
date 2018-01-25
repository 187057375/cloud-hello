package com.mycompany.cloud.controller.test.hadoop;

/**
 * 排序demo
 * @author peter
 * @version V1.0 创建时间：18/1/17
 *          Copyright 2018 by PreTang
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class MapReduceSort {
    public static class Map extends Mapper<Object, Text, IntWritable, IntWritable>{
        private static IntWritable data = new IntWritable();

        public void map(Object key,Text value, Context context) throws IOException,InterruptedException{
            String line =value.toString();
            data.set(Integer.parseInt(line));
            context.write(data, new IntWritable(1));
        }
    }

    public static class Reduce extends Reducer<IntWritable ,IntWritable, IntWritable,IntWritable>{
        private static IntWritable linenum = new IntWritable(1);

        public void reduce(IntWritable key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
            for(IntWritable val:values)
            {
                context.write(linenum, key);
                linenum = new IntWritable(linenum.get()+1);
            }
        }
    }


    public static class Partition extends Partitioner<IntWritable,IntWritable>{
        @Override
        public int getPartition(IntWritable key, IntWritable value, int numPartitions){
            int maxnumber = 65536;
            int minnumber = -3000;
            int bound = (maxnumber-minnumber)/numPartitions+1;
            int keynumber = key.get();
            for(int i=1;i<=numPartitions;i++){
                if(keynumber < minnumber){
                    return 0;
                }
                if(keynumber>=minnumber && keynumber<minnumber+i*bound){
                    return i;
                }
            }
            return numPartitions+1;
        }
    }

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Sort");
        job.setJarByClass(MapReduceSort.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setPartitionerClass(Partition.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
