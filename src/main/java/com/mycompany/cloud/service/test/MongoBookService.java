package com.mycompany.cloud.service.test;

import com.mycompany.cloud.domain.test.MongoBook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peter
 * @version V1.0 创建时间：17/10/30
 *          Copyright 2017 by PreTang
 */
@Component
public class MongoBookService {

    @Autowired
    MongoBookRepository mongoBookRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<MongoBook> findByQuery(String firstname){
        return mongoBookRepository.findByQuery(firstname);
    }

    public Map<String, Object> findByTemplate(String firstname){
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(firstname)) {
            criteria.orOperator(Criteria.where("firstname").regex(".*?" + firstname + ".*"));
        }
        Query query = new Query(criteria);
        query.skip((1 - 1) * 10);
        query.limit(10);
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "firstname")));
        List<MongoBook> modules = this.mongoTemplate.find(query, MongoBook.class);
        long count = this.mongoTemplate.count(query, MongoBook.class);
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("count", count);
        result.put("data", modules);
        return result;
    }
}
