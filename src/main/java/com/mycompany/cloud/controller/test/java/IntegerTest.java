package com.mycompany.cloud.controller.test.java;

/**
 * @author peter
 * @version V1.0 创建时间：18/2/25
 *          Copyright 2018 by 。。。
 */
public class IntegerTest {


    public static void main(String[] args) {
        int i = 128;
        Integer i2 = 128;
        Integer i3 = new Integer(128);
        System.out.println(i == i2);//true
        System.out.println(i == i3);//true
        System.out.println(i2 == i3);//false
        System.out.println("**************");

        Integer i5 = 127;
        Integer i6 = 127;
        System.out.println(i5 == i6);//true
        System.out.println("**************");

        Integer _i5 = 128;
        Integer _i6 = 128;
        System.out.println(_i5 == _i6);//false
        System.out.println("**************");

        Integer i7 = new Integer(128);
        Integer i8 = new Integer(128);
        System.out.println(i7 == i8);//false
    }

}
