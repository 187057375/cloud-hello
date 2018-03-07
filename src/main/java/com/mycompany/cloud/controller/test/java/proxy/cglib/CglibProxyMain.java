package com.mycompany.cloud.controller.test.java.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

//JDK中的动态代理通过反射类Proxy和InvocationHandler回调接口实现
// 要求委托类必须实现一个接口，只能对该类接口中定义的方法实现代理
// 这在实际编程中有一定的局限性
//使用cglib动态代理并不要求委托类必须实现接口，底层采用asm字节码生成框架生成代理类的字节码

public class CglibProxyMain {
    public static void main(String[] args) {
//        利用Enhancer类生成代理类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(new MyMethodInterceptor());
        UserServiceImpl userService = (UserServiceImpl) enhancer.create();

        userService.add();
    }
}

//代理对象的生成过程由Enhancer类实现，大概步骤如下：
//生成代理类Class的二进制字节码；
//通过Class.forName加载二进制字节码，生成Class对象；
//通过反射机制获取实例构造，并初始化代理类对象。


//Enhancer是CGLib的字节码增强器，可以方便的对类进行扩展
