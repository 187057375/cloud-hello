package com.mycompany.cloud.controller.test.java.proxy1.simple;


import com.mycompany.cloud.controller.test.java.proxy1.simple.impl.HelloProxy;


public class Main {
	public static void main(String[] args) {
		Hello helloProxy = new HelloProxy();
		helloProxy.say("hi");
	}
}
