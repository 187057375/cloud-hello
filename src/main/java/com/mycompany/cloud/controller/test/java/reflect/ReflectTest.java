package com.mycompany.cloud.controller.test.java.reflect;

import com.mycompany.cloud.controller.test.DemoController;

import java.lang.reflect.Method;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/7
 *          Copyright 2018 by PreTang
 */
public class ReflectTest {


    public static void main(String[] args) throws Exception {
        //1.如何创建Class的实例（重点）
        //1.调用运行时类的本身.class属性
        Class cls1 = DemoController.class;
        System.out.println(cls1);
        //2.通过运行时类的对象获取
        DemoController p = new DemoController();
        Class cls2 = p.getClass();
        System.out.println(cls2);
        //3.（常用）通过Class的静态方法获取，并通过此方法，体会反射的动态性
        Class cls3 = Class.forName("com.mycompany.cloud.controller.test.DemoController");
        System.out.println(cls3);


        Object obj = cls1.newInstance();
        DemoController a = (DemoController)obj;
        a.hello();

        for(Method method: DemoController.class.getMethods()){
            System.out.println(method.getName());
        }
    }
}
