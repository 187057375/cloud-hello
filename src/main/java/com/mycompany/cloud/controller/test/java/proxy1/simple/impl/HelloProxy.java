package com.mycompany.cloud.controller.test.java.proxy1.simple.impl;


import com.mycompany.cloud.controller.test.java.proxy1.simple.Hello;

/**
 *
 */
public class HelloProxy implements Hello {

	private Hello hello;

	public HelloProxy() {
		hello = new HelloImpl();
	}

	@Override
	public void say(String name) {
		before();
		hello.say(name);
		after();
	}

	private void before() {
		System.out.println("Before...");
	}

	private void after() {
		System.out.println("After...");
	}
}