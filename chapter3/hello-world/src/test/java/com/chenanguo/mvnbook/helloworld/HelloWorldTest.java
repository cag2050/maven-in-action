package com.chenanguo.mvnbook.helloworld;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldTest {

    @Test
    public void testSayHello() {
        HelloWorld helloWorld=new HelloWorld();
        String result=helloWorld.sayHello();
        assertEquals("Hello Maven", result);
    }
}
