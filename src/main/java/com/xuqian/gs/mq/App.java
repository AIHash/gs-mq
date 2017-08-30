package com.xuqian.gs.mq;

import com.xuqian.gs.mq.spring.GsMQContainer;

public class App {
    public static void main(String[] args) {
        new GsMQContainer().start();
    }
}
