package com.mycompany.cloud.controller.test.java.proxy;

/**
 * 真正的实现类
 */
public class UserInterfaceImpl implements UserInterface {
    @Override
    public void getName() {
        System.out.println("my name is zhenzhen");
    }

    @Override
    public String getNameById(String id) {
        System.out.println("args id:" + id);
        return "zhenzhen";
    }
}
