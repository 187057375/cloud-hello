package com.mycompany.cloud.controller.test.idgenarator;

import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/23
 *          Copyright 2018 by PreTang
 */
public class IdGeneratorRedisIncr {

    public static void main(String[] args) {
        IdGeneratorRedisIncr idg2=new IdGeneratorRedisIncr();
        String ooo = null;
        try {
            ooo = idg2.generateCode("myDisttriId","order_",true,10);
            System.out.println(ooo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key redis中的key值
     * @param prefix 最后编码的前缀
     * @param hasExpire redis是否使用过期时间生成自增id
     * @param minLength redis生成的自增id的最小长度，如果小于这个长度前面补0
     * @return
     */
    private synchronized String generateCode(String key,String prefix,boolean hasExpire,Integer minLength) throws  Exception{
        Date date = null;
        Long id = null;
        if(hasExpire){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            date = calendar.getTime();
        }

        //创建Jedis客户端
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String idNow = jedis.get(key);
        if(idNow == null){
            jedis.set(key,"0");
            if(hasExpire){
                jedis.expireAt(key,date.getTime());
            }
        }
        jedis.incr(key);//让用户刘德华年龄增加一岁
        idNow = jedis.get(key);
        String bizId = this.format(Long.valueOf(idNow),prefix,date,minLength);
        return bizId;
    }

//设定格式

    private String format(Long id,String prefix,Date date,Integer minLength){
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        if(date != null){
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            sb.append(df.format(date));
        }
        String strId =  String.valueOf(id);
        int length = strId.length();
        if(length < minLength){
            for(int i = 0 ;i < minLength - length; i++){
                sb.append("0");
            }
            sb.append(strId);
        }else{
            sb.append(strId);
        }
        return sb.toString();
    }
}
