package com.mycompany.cloud.controller.test.java.proxy2;

/**
 * 真正的实现类
 */
public class UserInterfaceImpl implements UserInterface {
    @Override
    public void getName() {
        System.out.println("my name is 真正的实现类");
    }

    @Override
    public String getNameById(String id) {
        System.out.println("args id:" + id);
        return "真正的实现类";
    }
}
