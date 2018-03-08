package com.mycompany.cloud.controller.test.java.proxy2.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * java 动态代理  代理对象必须是接口的实现类
 */
public class DynamicProxyHandler implements InvocationHandler {
    //这个就是我们要代理的真实对象
    private Object obj;

    //构造方法，给我们要代理的真实对象赋初值
    public DynamicProxyHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在代理真实对象前我们可以添加一些自己的操作
        System.out.println("proxy:before---" + method.getName() + "----------------");
        //当代理对象调用真实对象的方法时，其会自动跳转到代理对象关联的handler对象的invoke方法来进行调用
        method.invoke(obj, args);
        //在代理真实对象后我们可以添加一些自己的操作
        System.out.println("proxy:after---" + method.getName() + "----------------");
        return null;
    }
}
