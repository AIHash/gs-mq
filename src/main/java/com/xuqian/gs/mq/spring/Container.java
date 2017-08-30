
package com.xuqian.gs.mq.spring;

public interface Container {

    void start();

    void stop();

    Context<?> getContext();
}
