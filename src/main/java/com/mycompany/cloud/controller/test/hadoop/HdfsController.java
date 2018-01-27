package com.mycompany.cloud.controller.test.hadoop;

import com.mycompany.cloud.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * 演示Hadoop
 * @author peter
 * @version V1.0 创建时间：18/1/17
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/hadoop/hdfs/")
public class HdfsController extends BaseController {



    private Log LOGGER = LogFactory.getLog(HdfsController.class);

    @RequestMapping(value = "/hello" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        try{

            //hdfs dfs -mkdir -p /user/peter
            //hdfs dfs -put /etc/hosts /user/peter/test.txt

            //创建configuration对象
            Configuration conf = new Configuration();
            //创建FileSystem对象
            //需求：查看hdfs集群服务器/user/peter/test.txt内容
            FileSystem fs = FileSystem.get(URI.create("hdfs://localhost:8020/user/peter/test.txt"), conf);
            FSDataInputStream is = fs.open(new Path("hdfs://localhost:8020/user/peter/test.txt"));
            //OutputStream os=new FileOutputStream(new File("D:/a.txt"));
            byte[] buff= new byte[1024];
            int length = 0;
            while ((length=is.read(buff))!=-1){
                System.out.println(new String(buff,0,length));
                //os.write(buff,0,length);
                //os.flush();
            }
            System.out.println(fs.getClass().getName());


            result.put("result", true);
            result.put("msg", "test成功");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

    public void test2() throws  Exception {
       //1）构建Configuration对象，读取并解析相关配置文件
       Configuration conf = new Configuration();
       //2）设置相关属性
       conf.set("fs.defaultFS", "hdfs://localhost:8020");
       //3）获取特定文件系统实例fs（以HDFS文件系统实例）
       FileSystem fs = FileSystem.get(new URI("hdfs://localhost:8020"),conf,"hdfs");
       //4）通过文件系统实例fs进行文件操作(以删除文件实例)
       //fs.delete(new Path("/user/peter/test.txt"));

        //打开文件并打印
        FSDataInputStream is = fs.open(new Path("/user/peter/test.txt"));
        byte[] buff= new byte[1024];
        int length = 0;
        while ((length=is.read(buff))!=-1){
            System.out.println(new String(buff,0,length));
            //os.write(buff,0,length);
            //os.flush();
        }

        // 在hdfs的/user/peter目录下创建一个文件，并写入一行文本
//        FSDataOutputStream os = fs.create(new Path("/user/peter/hhhh.log"));
//        os.write("Hello World!".getBytes());
//        os.flush();
//        os.close();

        // 列出hdfs上/user/peter/目录下的所有文件和目录
        FileStatus[] statuses = fs.listStatus(new Path("/user/peter"));
        for (FileStatus status : statuses) {
            System.out.println(status);
        }

    }

    public static void main(String[] args) throws Exception {
        HdfsController test = new HdfsController();
        //test.hello();
        test.test2();
    }
}
