package com.mycompany.cloud.controller.test.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */

public class HbaseCrud {


    public static Configuration configuration = null;
    static
    {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "127.0.0.1:2181");
        //configuration.set("hbase.master", "192.168.0.201:60000");
        //configuration.set("hbase.zookeeper.property.clientPort", "2181");
    }

    //比较过滤器
    public void filterTest(String tablename){
        Scan scan=new Scan();//扫描器
        scan.setCaching(1000);//缓存1000条数据,一次读取1000条
        RowFilter filter =new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator("Jack".getBytes()));
        RowFilter filter1 =new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("J\\w+"));
        scan.setFilter(filter);
        try {
            HTable table = new HTable(configuration, tablename);
            ResultScanner scanner=table.getScanner(scan);//返回迭代器
            for(Result res:scanner){
                String rowkey= Bytes.toString(res.getRow());
                KeyValue[] kvs=res.raw();
                System.out.println("----");
                for (KeyValue kv:kvs){
                    String family= Bytes.toString(kv.getFamily());
                    String qualifier= Bytes.toString(kv.getQualifier());
                    String value =  Bytes.toString(kv.getValue());
                    System.out.println("rowkey->"+rowkey+"family->"+qualifier+"value->"+value);
                }
                System.out.println("----");
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public   void selectRowKey(String tablename, String rowKey) throws IOException
    {
        HTable table = new HTable(configuration, tablename);
        Get g = new Get(rowKey.getBytes());
        Result rs = table.get(g);
        Result result=table.get(g);
        for(Cell cell:result.listCells()){
            System.out.println("--------------------" + Bytes.toString(CellUtil.cloneRow(cell)) + "----------------------------");
            System.out.println("Column Family: " + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("Column       :" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("value        : " + Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println("value        : " + Bytes.toString(cell.getValueArray()));
        }


        for (KeyValue kv : rs.raw())
        {
            System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
            System.out.println("Column Family: " + new String(kv.getFamily()));
            System.out.println("Column       :" + new String(kv.getQualifier()));
            System.out.println("value        : " + new String(kv.getValue()));
        }
    }

    public   void selectAll(String tablename) throws IOException
    {
        Scan scan=new Scan();//扫描器
        //scan.addFamily(Bytes.toBytes("mobile"));
        scan.setCaching(1000);//缓存1000条数据,一次读取1000条
        try {
            System.out.println("2-------------------------");
            HTable table = new HTable(configuration, tablename);
            ResultScanner scanner=table.getScanner(scan);//返回迭代器
            for(Result result:scanner){
                String rowkey= Bytes.toString(result.getRow());
                KeyValue[] kvs=result.raw();
                for(Cell cell:result.rawCells()){
                    System.out.println("--------------------" + Bytes.toString(CellUtil.cloneRow(cell)) + "----------------------------");
                    System.out.println("Column Family: " + Bytes.toString(CellUtil.cloneFamily(cell)));
                    System.out.println("Column       :" + Bytes.toString(CellUtil.cloneQualifier(cell)));
                    System.out.println("value        : " + Bytes.toString(CellUtil.cloneValue(cell)));
                }

//                for (KeyValue kv : result.raw())
//                {
//                    System.out.println("--------------------" + new String(kv.getRow()) + "----------------------------");
//                    System.out.println("Column Family: " + new String(kv.getFamily()));
//                    System.out.println("Column       :" + new String(kv.getQualifier()));
//                    System.out.println("value        : " + new String(kv.getValue()));
//                }

            }
            System.out.println("-------------------------");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    public static void createTable(String tableName) {
        System.out.println("start create table ......");
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
            if (hBaseAdmin.tableExists(tableName)) {// 如果存在要创建的表，那么先删除，再创建
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + " is exist,detele....");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            tableDescriptor.addFamily(new HColumnDescriptor("column1"));
            tableDescriptor.addFamily(new HColumnDescriptor("column2"));
            tableDescriptor.addFamily(new HColumnDescriptor("column3"));
            hBaseAdmin.createTable(tableDescriptor);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end create table ......");
    }
    public static void insertData(String tableName) throws IOException{
        System.out.println("start insert data ......");
        HTable table = new HTable(configuration, tableName);
        //HTablePool pool = new HTablePool(configuration, 1000);
        //HTable table = (HTable) pool.getTable(tableName);
        Put put = new Put("22222222".getBytes());// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值

        put.add("column1".getBytes(), null, "aaa".getBytes());// 本行数据的第一列
        put.add("column2".getBytes(), null, "xxxx".getBytes());// 本行数据的第2列
        put.add("column3".getBytes(), null, "yyyyyy".getBytes());// 本行数据的第三列
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end insert data ......");
    }

    public static void dropTable(String tableName) {
        try {
            HBaseAdmin admin = new HBaseAdmin(configuration);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void deleteRow(String tablename, String rowkey)  {
        try {
            HTable table = new HTable(configuration, tablename);
            List list = new ArrayList();
            Delete d1 = new Delete(rowkey.getBytes());
            list.add(d1);

            table.delete(list);
            System.out.println("删除行成功!");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void deleteByCondition(String tablename, String rowkey)  {
        //目前还没有发现有效的API能够实现根据非rowkey的条件删除这个功能能，还有清空表全部数据的API操作

    }



    public static void QueryAll(String tableName) {
        HTablePool pool = new HTablePool(configuration, 1000);
        HTable table = (HTable) pool.getTable(tableName);
        try {
            ResultScanner rs = table.getScanner(new Scan());
            for (Result r : rs) {
                System.out.println("获得到rowkey:" + new String(r.getRow()));
                for (KeyValue keyValue : r.raw()) {
                    System.out.println("列：" + new String(keyValue.getFamily())
                            + "====值:" + new String(keyValue.getValue()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void QueryByCondition1(String tableName) {

        HTablePool pool = new HTablePool(configuration, 1000);
        HTable table = (HTable) pool.getTable(tableName);
        try {
            Get scan = new Get("abcdef".getBytes());// 根据rowkey查询
            Result r = table.get(scan);
            System.out.println("获得到rowkey:" + new String(r.getRow()));
            for (KeyValue keyValue : r.raw()) {
                System.out.println("列：" + new String(keyValue.getFamily())
                        + "====值:" + new String(keyValue.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void QueryByCondition2(String tableName) {

        try {
            HTablePool pool = new HTablePool(configuration, 1000);
            HTable table = (HTable) pool.getTable(tableName);
            Filter filter = new SingleColumnValueFilter(Bytes
                    .toBytes("column1"), null, CompareFilter.CompareOp.EQUAL, Bytes
                    .toBytes("aaa")); // 当列column1的值为aaa时进行查询
            Scan s = new Scan();
            s.setFilter(filter);
            ResultScanner rs = table.getScanner(s);
            for (Result r : rs) {
                System.out.println("获得到rowkey:" + new String(r.getRow()));
                for (KeyValue keyValue : r.raw()) {
                    System.out.println("列：" + new String(keyValue.getFamily())
                            + "====值:" + new String(keyValue.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void QueryByCondition3(String tableName) {

        try {
            HTablePool pool = new HTablePool(configuration, 1000);
            HTable table = (HTable) pool.getTable(tableName);

            List<Filter> filters = new ArrayList<Filter>();

            Filter filter1 = new SingleColumnValueFilter(Bytes
                    .toBytes("column1"), null, CompareFilter.CompareOp.EQUAL, Bytes
                    .toBytes("aaa"));
            filters.add(filter1);

            Filter filter2 = new SingleColumnValueFilter(Bytes
                    .toBytes("column2"), null, CompareFilter.CompareOp.EQUAL, Bytes
                    .toBytes("bbb"));
            filters.add(filter2);

            Filter filter3 = new SingleColumnValueFilter(Bytes
                    .toBytes("column3"), null, CompareFilter.CompareOp.EQUAL, Bytes
                    .toBytes("ccc"));
            filters.add(filter3);

            FilterList filterList1 = new FilterList(filters);

            Scan scan = new Scan();
            scan.setFilter(filterList1);
            ResultScanner rs = table.getScanner(scan);
            for (Result r : rs) {
                System.out.println("获得到rowkey:" + new String(r.getRow()));
                for (KeyValue keyValue : r.raw()) {
                    System.out.println("列：" + new String(keyValue.getFamily())
                            + "====值:" + new String(keyValue.getValue()));
                }
            }
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) throws Exception {
        HbaseCrud test = new HbaseCrud();
        //test.createTable("my_t1");
        //test.insertData("my_t1");
        test.selectAll("my_t1");
    }
}