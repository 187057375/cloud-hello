package com.mycompany.cloud.service.test;

/**
 * @author peter
 * @version V1.0 创建时间：17/10/18
 *          Copyright 2017 by PreTang
 */


import com.mycompany.cloud.domain.test.EsBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsBookRepository extends ElasticsearchRepository<EsBook, String> {

    Page<EsBook> findByAuthor(String author, Pageable pageable);

    List<EsBook> findByTitle(String title);

}