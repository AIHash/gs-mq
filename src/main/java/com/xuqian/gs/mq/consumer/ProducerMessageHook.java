package com.xuqian.gs.mq.consumer;

import com.xuqian.gs.mq.msg.ConsumerAckMessage;
import com.xuqian.gs.mq.msg.Message;

public interface ProducerMessageHook {

    ConsumerAckMessage hookMessage(Message paramMessage);
}
