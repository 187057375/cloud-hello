package com.mycompany.cloud.controller.test.java.proxy1.jdk;

import com.mycompany.cloud.controller.test.java.proxy1.jdk.impl.HelloImpl;
import com.mycompany.cloud.controller.test.java.proxy1.jdk.dynamicProxy.DynamicProxy;


/**
 *
 */
public class Main {
	public static void main(String[] args) {
		DynamicProxy dynamicProxy = new DynamicProxy(new HelloImpl());
		Hello helloProxy = dynamicProxy.getProxy();

		helloProxy.say("hi jdk proxy");
	}
}
