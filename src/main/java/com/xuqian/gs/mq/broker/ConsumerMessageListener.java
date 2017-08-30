package com.xuqian.gs.mq.broker;

import com.xuqian.gs.mq.model.RemoteChannelData;
import com.xuqian.gs.mq.msg.SubscribeMessage;

public interface ConsumerMessageListener {
    void hookConsumerMessage(SubscribeMessage msg, RemoteChannelData channel);
}
