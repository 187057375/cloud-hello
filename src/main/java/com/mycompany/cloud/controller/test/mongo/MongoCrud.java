package com.mycompany.cloud.controller.test.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/24
 *          Copyright 2018 by PreTang
 */
public class MongoCrud {


    static MongoClient connection=null;

    static MongoDatabase db=null;

    public MongoCrud(String dataBaseName){
        connection=new MongoClient("127.0.0.1",27017);
        db=connection.getDatabase(dataBaseName);
    }
    public MongoCrud(){}
    /**
     * 创建一个数据库集合
     * @Title: createCollection
     * @Description: TODO
     * @param collName 要创建的集合名称
     * @return: void
     */
    public void createCollection(String collName){
        db.createCollection(collName, new CreateCollectionOptions());
    }

    /**
     * 插入单行数据
     * @Title: insert
     * @Description: TODO
     * @param collName 要插入的集合的名称
     * @param doc 插入的Document对象
     * @return: void
     */
    public void insert(String collName,Document doc){
        MongoCollection<Document> curColl = db.getCollection(collName);
        curColl.insertOne(doc);
    }

    /**
     * 插入多行数据
     * @Title: insertMany
     * @Description: TODO
     * @param collName 集合名称
     * @param list 要插入的集合数据
     * @return: void
     */
    public void insertMany(String collName,List<Document>list){
        MongoCollection<Document> curColl = db.getCollection(collName);
        curColl.insertMany(list);
    }

    /**
     * 将iterator转换为list集合
     * @Title: dealItToList
     * @Description: TODO
     * @return
     * @return: List<Document>
     */
    public List<Document> dealItToList(FindIterable<Document> itDoc){
        List<Document> list=new ArrayList<>();
        Iterator<Document> it=itDoc.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    /**
     * 通过集合名称获取集合
     * @Title: getData
     * @Description: TODO
     * @param collName 集合名称
     * @return
     * @return: List<Document>
     */
    public List<Document> getData(String collName){
        MongoCollection<Document> curColl = db.getCollection(collName);
        FindIterable<Document> docs = curColl.find();
        return dealItToList(docs);
    }


    /**
     * 执行更新操作
     * @Title: updateDocument
     * @Description: TODO
     * @param collName 集合名称
     * @param whereBson 条件对象 相当于  where对应的部分
     * @param setBson   内容对象  相当于 set对应的部分
     * @return: void
     */
    public void updateDocument(String collName, Bson whereBson, Bson setBson){
        MongoCollection<Document>curColl=db.getCollection(collName);
        curColl.updateOne(whereBson, setBson);
    }

    /**
     * 通过id删除对应数据
     * @Title: deleteById
     * @Description: TODO
     * @param collName 集合名称
     * @param id 记录id
     * @return: void
     */
    public void deleteById(String collName,String id){
        MongoCollection<Document>curColl=db.getCollection(collName);
        Bson bson=new BasicDBObject("_id",id);
        curColl.deleteOne(bson);
    }

    /**
     * mongo driver 测试
     * @param args
     */
    public static void main(String[] args) {
        MongoCrud mongoDB=new MongoCrud("foobar");
        //mongoDB.createCollection("tbf_users");
        //mongoDB.insert("tbf_user", new Document("name","jack"));
		/*Map<String, Object> map=new HashMap<>();
		map.put("place", "map");
		map.put("ifuse", true);
		mongoDB.insert("tbf_user", new Document(map));;*/
		/*List<Document> list=mongoDB.getData("persons");
		mongoDB.insertMany("tbf_user", list);*/
		/*mongoDB.deleteById("tbf_user", "123");*/
        Bson whereBson=new BasicDBObject("_id","245");
        Bson setBson=new BasicDBObject("$set",new BasicDBObject("name","jack"));
        mongoDB.updateDocument("tbf_user",whereBson,setBson);
    }
}
