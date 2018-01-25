package com.mycompany.cloud.service.test;


import com.mycompany.cloud.domain.test.MongoBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author peter
 * @version V1.0 创建时间：17/10/30
 *          Copyright 2017 by PreTang

 * MongoBookService<MongoBook, Integer>
 * 第一个参数：T 操作的vo
 * 第二个参数：ID T的主键类型
 * 作用：该接口实现了CRUD方法
 *
 * 注意：
 * 1、由于boot使用了spring-data-mongodb，所以我们不需要写该接口的实现，
 *   当我们运行程序的时候，spring-data-mongodb会动态创建
 * 2、findBySecondname命名是有讲究的，Secondname（是MongoBook的属性）若改为lastname就会报找不到属性lastname的错误
 */
public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
    public MongoBook findByFirstname(String firstname);

    public List<MongoBook> findBySecondname(String secondname);

    @Query(value="{ 'firstname' : ?0}")
    public List<MongoBook> findByQuery(String firstname);
}