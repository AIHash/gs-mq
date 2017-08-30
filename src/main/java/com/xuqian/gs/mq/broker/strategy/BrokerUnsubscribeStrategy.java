package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.consumer.ConsumerContext;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.UnSubscribeMessage;
import io.netty.channel.ChannelHandlerContext;

public class BrokerUnsubscribeStrategy implements BrokerStrategy {

    public BrokerUnsubscribeStrategy() {

    }

    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        UnSubscribeMessage msgUnSubscribe = (UnSubscribeMessage) request.getMsgParams();
        ConsumerContext.unLoad(msgUnSubscribe.getConsumerId());
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {

    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {

    }

    public void setChannelHandler(ChannelHandlerContext channelHandler) {

    }
}
