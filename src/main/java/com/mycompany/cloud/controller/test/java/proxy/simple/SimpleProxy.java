package com.mycompany.cloud.controller.test.java.proxy.simple;

import com.mycompany.cloud.controller.test.java.proxy.UserInterface;

/**
 * java 静态代理
 */
public class SimpleProxy implements UserInterface {
    private UserInterface iface;

    public SimpleProxy(UserInterface iface) {
        this.iface = iface;
    }

    @Override
    public void getName() {
        System.out.println("proxy---------getName-------------");
        iface.getName();
    }

    @Override
    public String getNameById(String id) {
        System.out.println("proxy---------getNameById-------------");
        return iface.getNameById(id);
    }
}
