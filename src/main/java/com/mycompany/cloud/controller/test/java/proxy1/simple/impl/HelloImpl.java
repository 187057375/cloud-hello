package com.mycompany.cloud.controller.test.java.proxy1.simple.impl;


import com.mycompany.cloud.controller.test.java.proxy1.simple.Hello;

/**
 *
 */
public class HelloImpl implements Hello{
	@Override
	public void say(String name) {
		System.out.println("Hello! " + name);
	}
}
