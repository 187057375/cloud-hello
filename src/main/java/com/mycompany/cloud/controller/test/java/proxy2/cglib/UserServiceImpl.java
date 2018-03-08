package com.mycompany.cloud.controller.test.java.proxy2.cglib;

//定义业务逻辑
public class UserServiceImpl {
    public void add() {
        System.out.println("This is add service");
    }

    public void delete(int id) {
        System.out.println("This is delete service：delete " + id);
    }
}
