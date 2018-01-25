package com.mycompany.cloud.controller.test;

import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.service.test.HbaseTestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/hbase")
public class HbaseController extends BaseController {


    @Resource
    private HbaseTestService hbaseTestService ;
    Log LOGGER = LogFactory.getLog(HbaseController.class);


    @RequestMapping(value = "/hbase2",   produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object hbase2() throws Exception {
        hbaseTestService.selectRowKey("t1","row1");
        return null;
    }

    @RequestMapping(value = "/hbase3",   produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object hbase3() throws Exception {

        hbaseTestService.selectAll("t1");
        return null;
    }

    @RequestMapping(value = "/hbase4",   produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object hbase4() throws Exception {

        hbaseTestService.createTable("t3");
        return null;
    }
}
