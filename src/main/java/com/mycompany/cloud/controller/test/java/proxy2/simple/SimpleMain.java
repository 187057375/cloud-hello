package com.mycompany.cloud.controller.test.java.proxy2.simple;

import com.mycompany.cloud.controller.test.java.proxy2.UserInterface;
import com.mycompany.cloud.controller.test.java.proxy2.UserInterfaceImpl;

public class SimpleMain {
    private static void consume(UserInterface iface) {
        iface.getName();
        String name = iface.getNameById("1");
        System.out.println("name: " + name);
    }

    public static void main(String[] args) {
        System.out.println("不代理------------------------------------");
        consume(new UserInterfaceImpl());
        System.out.println("代理------------------------------------");
        consume(new SimpleProxy(new UserInterfaceImpl()));
    }
}
