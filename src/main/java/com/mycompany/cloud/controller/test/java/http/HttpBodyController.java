package com.mycompany.cloud.controller.test.java.http;

import com.mycompany.cloud.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 演示框架代码
 * @author peter
 * @version V1.0 创建时间：17/8/31
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/java/http/httpbody")
public class HttpBodyController extends BaseController {


    Log LOGGER = LogFactory.getLog(HttpBodyController.class);

    @ResponseBody
    @RequestMapping(value = "/testRequestBody")
    public Map<String, Object> testRequestBody(@RequestBody Map<String, Object> map) {
        System.out.println("================"+map);
        return map;
    }






}
