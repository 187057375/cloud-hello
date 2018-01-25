package com.mycompany.cloud.controller.test;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.mycompany.cloud.controller.BaseController;
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
@RequestMapping("/test/tencentSms")
public class TencentSmsController extends BaseController {



    private Log LOGGER = LogFactory.getLog(TencentSmsController.class);

    @Autowired
    private TestNewsMybatisDao testNewsMybatisDao;

    @RequestMapping(value = "/send" ,method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> send(Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            SmsSingleSender sender = new   SmsSingleSender(1400062132, "8e2ecfcbbe777a3ce5e01f9983114753");
            SmsSingleSenderResult result1 = sender.send(0, "86", "18326693192", "模板示例：3333为您的登录验证码，请于2分钟内填写。如非本人操作，请忽略本短信。", "", "123");
            System.out.print(result1);
            result.put("result", true);
            result.put("data", "ok");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }
}
