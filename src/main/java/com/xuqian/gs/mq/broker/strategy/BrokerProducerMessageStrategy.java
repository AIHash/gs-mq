package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.Message;
import io.netty.channel.ChannelHandlerContext;

public class BrokerProducerMessageStrategy implements BrokerStrategy {

    private ProducerMessageListener hookProducer;
    private ChannelHandlerContext channelHandler;

    public BrokerProducerMessageStrategy() {

    }

    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        Message message = (Message) request.getMsgParams();
        hookProducer.hookProducerMessage(message, request.getMsgId(), channelHandler.channel());
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {
        this.hookProducer = hookProducer;
    }

    public void setChannelHandler(ChannelHandlerContext channelHandler) {
        this.channelHandler = channelHandler;
    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {

    }
}
