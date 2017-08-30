package com.xuqian.gs.mq.spring;

import com.xuqian.gs.mq.broker.server.AvatarMQBrokerServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class AvatarMQServer extends AvatarMQBrokerServer implements ApplicationContextAware, InitializingBean {

    private String serverAddress;

    public AvatarMQServer(String serverAddress) {
        super(serverAddress);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.printf("AvatarMQ Server Start Success![author tangjie]\n");
    }

    public void afterPropertiesSet() throws Exception {
        init();
        start();
    }
}
