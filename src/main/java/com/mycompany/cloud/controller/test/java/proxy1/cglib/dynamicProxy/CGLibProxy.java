package com.mycompany.cloud.controller.test.java.proxy1.cglib.dynamicProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxy implements MethodInterceptor {

    private static CGLibProxy instance = new CGLibProxy();

    private CGLibProxy(){
    }

    public static CGLibProxy getInstance(){
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cls){

        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] arg,
            MethodProxy proxy) throws Throwable {
        before();
        Object result = proxy.invokeSuper(obj, arg);
        after();
        return result;
    }

    private void before() {
        System.out.println("Before...");
    }

    private void after() {
        System.out.println("After...");
    }
}