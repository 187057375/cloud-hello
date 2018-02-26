package com.mycompany.cloud.controller.test.java.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mycompany.cloud.domain.test.NewsMb;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peter
 * @version V1.0 创建时间：18/2/26
 *          Copyright 2018 by PreTang
 */
public class HttpUtil {


    public static void main(String[] args) throws Exception {
        HttpUtil.httpPostJson();
    }

    public static void httpPostJson() {
        NewsMb nm = new NewsMb();
        nm.setShareTotal(33);
        String payout = JSON.toJSONString(nm, SerializerFeature.WriteMapNullValue);
        CloseableHttpClient closeHttpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        //发送Post请求
        HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/index/demo");
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("ss", "test123"));
        try {

            StringEntity entity = new StringEntity(payout, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            //执行Post请求 得到Response对象
            httpResponse = closeHttpClient.execute(httpPost);
            //httpResponse.getStatusLine() 响应头信息
            System.out.println(httpResponse.getStatusLine());
            //返回对象 向上造型
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                //响应输入流
                InputStream is = httpEntity.getContent();
                //转换为字符输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                //关闭输入流
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void httpPostMulte() {

    }

    public static void httpPost() {
        CloseableHttpClient closeHttpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        //发送Post请求
        HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/index/demo");
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("ss", "test123"));
        try {
            //转换参数并设置编码格式
            httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
            //执行Post请求 得到Response对象
            httpResponse = closeHttpClient.execute(httpPost);
            //httpResponse.getStatusLine() 响应头信息
            System.out.println(httpResponse.getStatusLine());
            //返回对象 向上造型
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                //响应输入流
                InputStream is = httpEntity.getContent();
                //转换为字符输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                //关闭输入流
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
