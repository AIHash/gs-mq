package com.xuqian.gs.mq;

import com.xuqian.gs.mq.spring.AvatarMQContainer;

public class App {
    public static void main(String[] args) {
        new AvatarMQContainer().start();
    }
}
