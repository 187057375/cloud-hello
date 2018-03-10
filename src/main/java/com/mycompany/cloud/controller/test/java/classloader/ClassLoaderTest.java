package com.mycompany.cloud.controller.test.java.classloader;

/**
 * @author peter
 * @version V1.0 创建时间：18/3/10
 *          Copyright 2018 by PreTang
 */
public class ClassLoaderTest {


    public static void main(String[] args) {

        ClassLoader cl = ClassLoaderTest.class.getClassLoader();

        System.out.println("ClassLoader is:"+cl.toString());

    }
}
