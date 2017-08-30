package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.broker.SendMessageLauncher;
import com.xuqian.gs.mq.core.CallBackInvoker;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;


public class BrokerConsumerMessageStrategy implements BrokerStrategy {

    public BrokerConsumerMessageStrategy() {

    }

    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        String key = response.getMsgId();
        if (SendMessageLauncher.getInstance().trace(key)) {
            CallBackInvoker<Object> future = SendMessageLauncher.getInstance().detach(key);
            if (future == null) {
                return;
            } else {
                future.setMessageResult(request);
            }
        } else {
            return;
        }
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {

    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {

    }

    public void setChannelHandler(ChannelHandlerContext channelHandler) {

    }
}
