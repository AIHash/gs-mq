package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.model.MessageType;
import com.xuqian.gs.mq.model.RemoteChannelData;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.SubscribeMessage;
import io.netty.channel.ChannelHandlerContext;

public class BrokerSubscribeStrategy implements BrokerStrategy {

    private ConsumerMessageListener hookConsumer;
    private ChannelHandlerContext channelHandler;

    public BrokerSubscribeStrategy() {

    }

    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        SubscribeMessage subcript = (SubscribeMessage) request.getMsgParams();
        String clientKey = subcript.getConsumerId();
        RemoteChannelData channel = new RemoteChannelData(channelHandler.channel(), clientKey);
        hookConsumer.hookConsumerMessage(subcript, channel);
        response.setMsgType(MessageType.AvatarMQConsumerAck);
        channelHandler.writeAndFlush(response);
    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {
        this.hookConsumer = hookConsumer;
    }

    public void setChannelHandler(ChannelHandlerContext channelHandler) {
        this.channelHandler = channelHandler;
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {

    }
}
