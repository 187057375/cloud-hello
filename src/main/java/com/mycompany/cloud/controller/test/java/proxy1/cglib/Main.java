package com.mycompany.cloud.controller.test.java.proxy1.cglib;


import com.mycompany.cloud.controller.test.java.proxy1.cglib.dynamicProxy.CGLibProxy;
import com.mycompany.cloud.controller.test.java.proxy1.cglib.impl.HelloImpl;

public class Main {
	public static void main(String[] args) {
		Hello helloProxy = CGLibProxy.getInstance().getProxy(HelloImpl.class);

		helloProxy.say("hi");
	}
}