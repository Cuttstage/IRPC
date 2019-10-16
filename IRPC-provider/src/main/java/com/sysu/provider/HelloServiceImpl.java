package com.sysu.provider;

import com.sysu.api.HelloService;

public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "Hello" + name;
    }
}
