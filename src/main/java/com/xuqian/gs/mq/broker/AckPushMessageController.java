package com.xuqian.gs.mq.broker;

import com.xuqian.gs.mq.core.AckMessageCache;
import com.xuqian.gs.mq.core.MessageSystemConfig;

import java.util.concurrent.Callable;

public class AckPushMessageController implements Callable<Void> {

    private volatile boolean stoped = false;

    public Void call() {
        AckMessageCache ref = AckMessageCache.getAckMessageCache();
        int timeout = MessageSystemConfig.AckMessageControllerTimeOutValue;
        while (!stoped) {
            if (ref.hold(timeout)) {
                ref.commit();
            }
        }
        return null;
    }

    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }
}
