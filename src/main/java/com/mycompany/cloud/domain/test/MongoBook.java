package com.mycompany.cloud.domain.test;

import org.springframework.data.annotation.Id;

/**
 * @author peter
 * @version V1.0 创建时间：17/10/25
 *          Copyright 2017 by PreTang
 */
public class MongoBook {


    @Id
    private String cid;
    private String firstname;
    private String secondname;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }
}
