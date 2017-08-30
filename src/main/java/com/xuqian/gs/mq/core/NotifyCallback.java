package com.xuqian.gs.mq.core;

import com.xuqian.gs.mq.msg.ProducerAckMessage;

public interface NotifyCallback {

    void onEvent(ProducerAckMessage result);
}
