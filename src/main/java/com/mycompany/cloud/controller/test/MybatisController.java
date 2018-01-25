package com.mycompany.cloud.controller.test;

import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.domain.test.NewsMb;
import com.mycompany.cloud.service.test.TestNewsMybatisDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/hbase")
public class MybatisController extends BaseController {



    private Log LOGGER = LogFactory.getLog(MybatisController.class);

    @Autowired
    private TestNewsMybatisDao testNewsMybatisDao;

    @RequestMapping(value = "/mybatis" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> mybatis(Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            NewsMb newsMb = testNewsMybatisDao.queryNewsMb(id);
            result.put("result", true);
            result.put("data", newsMb);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }
}
