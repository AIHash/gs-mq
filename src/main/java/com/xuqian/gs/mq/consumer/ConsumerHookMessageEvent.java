package com.xuqian.gs.mq.consumer;

import com.xuqian.gs.mq.core.HookMessageEvent;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.ConsumerAckMessage;
import com.xuqian.gs.mq.msg.Message;

public class ConsumerHookMessageEvent extends HookMessageEvent<Object> {

    private ProducerMessageHook hook;

    public ConsumerHookMessageEvent(ProducerMessageHook hook) {
        this.hook = hook;
    }

    public Object callBackMessage(Object obj) {
        ResponseMessage response = (ResponseMessage) obj;
        if (response.getMsgParams() instanceof Message) {
            ConsumerAckMessage result = hook.hookMessage((Message) response.getMsgParams());
            result.setMsgId(((Message) response.getMsgParams()).getMsgId());
            return result;
        } else {
            return null;
        }
    }
}
