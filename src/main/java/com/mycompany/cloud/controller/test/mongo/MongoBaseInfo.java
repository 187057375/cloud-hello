package com.mycompany.cloud.controller.test.mongo;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/24
 *          Copyright 2018 by PreTang
 */
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.List;
import java.util.Set;


public class MongoBaseInfo {

    public static void main(String[] args) {
        try {
            //建立一个 Mongo的数据库对象
            Mongo mongo=new Mongo("127.0.0.1:27017");
            //查询所有的DatabaseNames
            List<String> dbName=mongo.getDatabaseNames();
            for(String name:dbName){
                System.out.println(name);
            }

            System.out.println("----连接数据库kgj-log");
            //创建相关数据库的连接
            DB db=mongo.getDB("kgj-log");
            //查询数据库集合当中的所有名字
            Set<String> colName=db.getCollectionNames();
            for(String name1:colName){
                System.out.println("数据库集合"+name1);
            }
            //查询所有数据
            System.out.println("----连接数据表mongoBook");
            DBCollection dbcollection=db.getCollection("mongoBook");
            DBCursor cur= dbcollection.find();
            while(cur.hasNext()){
                DBObject object=cur.next();
                System.out.println("firtname="+object.get("firstname"));
                System.out.println(com.alibaba.fastjson.JSON.toJSONString(object.toMap()));
            }
            //其它操作
            System.out.println(cur.count());
            System.out.println(JSON.serialize(cur));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}