package com.mycompany.cloud.controller.test.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mycompany.cloud.controller.BaseController;
import com.mycompany.cloud.domain.test.MongoBook;
import com.mycompany.cloud.service.test.MongoBookRepository;
import com.mycompany.cloud.service.test.MongoBookService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * mongo演示
 */
@RestController
@RequestMapping("/test/demo")
public class MongoCrudController extends BaseController {


    @Autowired
    private MongoBookRepository mongoBookRepository;
    @Autowired
    private MongoBookService mongoBookService;

    private  Log LOGGER = LogFactory.getLog(MongoCrudController.class);

    ////mongo使用-demo
    @RequestMapping(value = "/addMongoBook", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public MongoBook addMongoBook(@RequestParam("firstname") String firstname, @RequestParam("secondname") String secondname) {
        //
        DBObject documents = new BasicDBObject("name", "张三")
                .append("age", 45)
                .append("sex", "男")
                .append("address", new BasicDBObject("postCode", 100000).append("street", "深南大道888号").append("city", "深圳"));


        //对象方式
        MongoBook customer = new MongoBook();
        customer.setFirstname(firstname);
        customer.setSecondname(secondname);
        return mongoBookRepository.save(customer);

    }

    @RequestMapping(value = "/getAllMongoBook", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> getAllMongoBook() {
        return mongoBookRepository.findAll();
    }

    @RequestMapping(value = "/getMongoBookByFirstname", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public MongoBook getMongoBookByFirstname(@RequestParam("firstname") String firstname) {
        return mongoBookRepository.findByFirstname(firstname);
    }

    @RequestMapping(value = "/getMongoBookBySecondname", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> getMongoBookBySecondname(@RequestParam("secondname") String secondname) {
        return mongoBookRepository.findBySecondname(secondname);
    }

    @RequestMapping(value = "/deleteMongoBookById", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public boolean deleteMongoBookById(@RequestParam("cid") String cid) {
        mongoBookRepository.delete(cid);
        return true;
    }

    @RequestMapping(value = "/findByQuery", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<MongoBook> findByQuery(@RequestParam("firstname") String firstname) {
        return mongoBookRepository.findByQuery(firstname);
    }

    @RequestMapping(value = "/findByTemplate", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Map<String, Object> findByTemplate(@RequestParam("firstname") String firstname) {
        return mongoBookService.findByTemplate(firstname);
    }
}
