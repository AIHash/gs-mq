package com.xuqian.gs.mq.netty;

public interface MessageEventProxy {

    void beforeMessage(Object msg);

    void afterMessage(Object msg);
}
