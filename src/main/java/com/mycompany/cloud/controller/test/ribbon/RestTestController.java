package com.mycompany.cloud.controller.test.ribbon;

import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.controller.test.DemoController;
import com.mycompany.cloud.domain.test.NewsMb;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * restTemplate.postForObject演示
 * @author peter
 * @version V1.0 创建时间：18/1/16
 *          Copyright 2017 by PreTang
 */
@RestController
@RequestMapping("/test/ribbon")
public class RestTestController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;
    Log LOGGER = LogFactory.getLog(DemoController.class);

    @RequestMapping(value = "/pp", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> pp()  {

        Map<String, Object> result = new HashMap<>();
        try {
            //直接传，applica/xml
            Map<String, Object> body = new HashMap<>();
            body.put("result", true);
            body.put("xx","dsad");

            //转化application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity  entity = new HttpEntity(new NewsMb(), headers);

            Map<String, Object> data  = restTemplate.postForObject("http://chutang-cloud-demo/test/demo/hh",entity,Map.class);
            result.put("result", true);
            result.put("data",data);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "/gg",  produces = "application/json; charset=utf-8")
    public Map<String, Object> gg(HttpServletRequest request,@RequestBody Map ppp) {

        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println(ppp.toString());
            System.out.println(request.getHeader("Content-Type"));
//            BufferedReader br = request.getReader();
//
//            String str, wholeStr = "";
//            while((str = br.readLine()) != null){
//                wholeStr += str;
//            }
//            System.out.println("===="+wholeStr);



            result.put("result", true);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            LOGGER.error("logtest错误", e);
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "/hh",  produces = "application/json; charset=utf-8")
    public Map<String, Object> hh(HttpServletRequest request,@RequestBody NewsMb news) {

        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println(news.getContent());
            System.out.println(request.getHeader("Content-Type"));

            result.put("result", true);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            LOGGER.error("logtest错误", e);
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "/ff",  produces = "application/json; charset=utf-8")
    public Map<String, Object> hh(HttpServletRequest request,@RequestBody String str) {

        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println(str);
            System.out.println(request.getHeader("Content-Type"));

            result.put("result", true);
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            LOGGER.error("logtest错误", e);
            e.printStackTrace();
        }
        return result;

    }
}
