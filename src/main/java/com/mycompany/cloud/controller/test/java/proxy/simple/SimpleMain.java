package com.mycompany.cloud.controller.test.java.proxy.simple;

import com.mycompany.cloud.controller.test.java.proxy.UserInterface;
import com.mycompany.cloud.controller.test.java.proxy.UserInterfaceImpl;

public class SimpleMain {
    private static void consume(UserInterface iface) {
        iface.getName();
        String name = iface.getNameById("1");
        System.out.println("name: " + name);
    }

    public static void main(String[] args) {
        consume(new UserInterfaceImpl());
        System.out.println("------------------------------------");
        consume(new SimpleProxy(new UserInterfaceImpl()));
    }
}
