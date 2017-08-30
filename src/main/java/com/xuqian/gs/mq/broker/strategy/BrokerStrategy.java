package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;


public interface BrokerStrategy {

    void messageDispatch(RequestMessage request, ResponseMessage response);

    void setHookProducer(ProducerMessageListener hookProducer);

    void setHookConsumer(ConsumerMessageListener hookConsumer);

    void setChannelHandler(ChannelHandlerContext channelHandler);
}
