package com.mycompany.cloud.controller.test.hadoop.hbase;

/**
 * 从hbase里取数据，分析完成后的数据插入到hbase里
 * @author peter
 * @version V1.0 创建时间：18/1/24
 *          Copyright 2018 by PreTang
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;


public class MapReduceHbase {

    public static class MyTestMap extends TableMapper<Text, IntWritable>{

        @Override
        protected void map(ImmutableBytesWritable key, Result result, Context context) throws IOException, InterruptedException {


            String rowkey= Bytes.toString(result.getRow());
            KeyValue[] kvs=result.raw();

            System.out.println("--------------------"+ Thread.currentThread().getId());
            System.out.println(result);
            for (KeyValue kv:kvs){
                String family= Bytes.toString(kv.getFamily());
                String qualifier= Bytes.toString(kv.getQualifier());
                String value =  Bytes.toString(kv.getValue());
                System.out.println("rowkey="+rowkey);
                System.out.println("family="+family);
                System.out.println("qualifier="+qualifier);
                System.out.println("value="+value);
            }
            System.out.println("--------------------");
            for (Cell cell : result.rawCells())
            {
                String myvalue =new String(CellUtil.cloneValue(cell));
                //System.out.println(new String(CellUtil.cloneFamily(cell)));
                //System.out.println(myvalue);
                if(myvalue.equals("aaa")){
                    context.write(new Text(myvalue), new IntWritable(1));
                }
            }
        }
    }

    public static class MyTestReduce extends   TableReducer<Text, IntWritable, NullWritable> {
        public void reduce(Text key, Iterable<IntWritable> values,  Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable i : values) {
                sum += i.get();
            }
            byte[] keyBytes = Bytes.toBytes(key.toString());
            if(keyBytes.length>0){
                Put put = new Put(keyBytes);
                // Put实例化，每一个词存一行
                put.addColumn(Bytes.toBytes("content"), Bytes.toBytes("count"),  Bytes.toBytes(String.valueOf(sum)));
                // 列族为content，列为count，列值为数目
                context.write(NullWritable.get(), put);
            }
        }
    }

    public static void createHBaseTable(String tableName) throws IOException {
        HTableDescriptor htd = new HTableDescriptor(tableName);
        HColumnDescriptor col = new HColumnDescriptor("content");
        htd.addFamily(col);
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","127.0.0.1:2181");
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table exists, trying to recreate table......");
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        System.out.println("create new table:" + tableName);
        admin.createTable(htd);
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        String resultTableName = "my_t1_sum_by_groupid";
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "127.0.0.1:2181");
        createHBaseTable(resultTableName);
        Job job = Job.getInstance(conf, "my_t1_sum_by_groupid");
        //Job job = new Job(conf, "my_t1_sum_by_groupid");
        job.setJarByClass(MapReduceHbase.class);
        //job.setNumReduceTasks(2);
        Scan scan = new Scan();
        scan.addFamily("column1".getBytes()); //只查询column1这列数据
        //TableInputFormat t = new TableInputFormat();
        TableMapReduceUtil.initTableMapperJob("my_t1", scan, MyTestMap.class, Text.class, IntWritable.class, job);
        TableMapReduceUtil.initTableReducerJob(resultTableName, MyTestReduce.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}