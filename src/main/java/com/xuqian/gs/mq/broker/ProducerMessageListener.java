
package com.xuqian.gs.mq.broker;

import com.xuqian.gs.mq.msg.Message;
import io.netty.channel.Channel;

public interface ProducerMessageListener {

    void hookProducerMessage(Message msg, String requestId, Channel channel);
}
