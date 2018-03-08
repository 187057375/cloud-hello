package com.mycompany.cloud.controller.test.java.proxy1.cglib.impl;

import com.mycompany.cloud.controller.test.java.proxy1.cglib.Hello;

/**
 *
 */
public class HelloImpl implements Hello{
	public void say(String name) {
		System.out.println("Hello! " + name);
	}
}
