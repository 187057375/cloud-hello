package com.mycompany.cloud.controller.test.java.proxy.dynamic;

import com.mycompany.cloud.controller.test.java.proxy.UserInterface;
import com.mycompany.cloud.controller.test.java.proxy.UserInterfaceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyMain {
    private static void consume(UserInterface iface) {
        iface.getName();
        String name = iface.getNameById("1");
        System.out.println("name: " + name);
    }

    public static void main(String[] args) {
        UserInterfaceImpl impl = new UserInterfaceImpl();
        //实现类的classload
        ClassLoader loader = UserInterfaceImpl.class.getClassLoader();
        //代理实现类
        InvocationHandler handler = new DynamicProxyHandler(impl);
        UserInterface iface = (UserInterface) Proxy.newProxyInstance(loader, new Class<?>[]{UserInterface.class}, handler);
        consume(iface);
    }
}
