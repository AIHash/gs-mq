package com.xuqian.gs.mq.broker.strategy;

import com.xuqian.gs.mq.broker.ConsumerMessageListener;
import com.xuqian.gs.mq.broker.ProducerMessageListener;
import com.xuqian.gs.mq.model.MessageSource;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class BrokerStrategyContext {

    public final static int AvatarMQProducerMessageStrategy = 1;
    public final static int AvatarMQConsumerMessageStrategy = 2;
    public final static int AvatarMQSubscribeStrategy = 3;
    public final static int AvatarMQUnsubscribeStrategy = 4;

    private RequestMessage request;
    private ResponseMessage response;
    private ChannelHandlerContext channelHandler;
    private ProducerMessageListener hookProducer;
    private ConsumerMessageListener hookConsumer;
    private BrokerStrategy strategy;

    private static Map strategyMap = TypedMap.decorate(new HashMap(), Integer.class, BrokerStrategy.class);

    static {
        strategyMap.put(AvatarMQProducerMessageStrategy, new BrokerProducerMessageStrategy());
        strategyMap.put(AvatarMQConsumerMessageStrategy, new BrokerConsumerMessageStrategy());
        strategyMap.put(AvatarMQSubscribeStrategy, new BrokerSubscribeStrategy());
        strategyMap.put(AvatarMQUnsubscribeStrategy, new BrokerUnsubscribeStrategy());
    }

    public BrokerStrategyContext(RequestMessage request, ResponseMessage response, ChannelHandlerContext channelHandler) {
        this.request = request;
        this.response = response;
        this.channelHandler = channelHandler;
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {
        this.hookProducer = hookProducer;
    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {
        this.hookConsumer = hookConsumer;
    }

    public void invoke() {
        switch (request.getMsgType()) {
            case AvatarMQMessage:
                strategy = (BrokerStrategy) strategyMap.get(request.getMsgSource() == MessageSource.AvatarMQProducer ? AvatarMQProducerMessageStrategy : AvatarMQConsumerMessageStrategy);
                break;
            case AvatarMQSubscribe:
                strategy = (BrokerStrategy) strategyMap.get(AvatarMQSubscribeStrategy);
                break;
            case AvatarMQUnsubscribe:
                strategy = (BrokerStrategy) strategyMap.get(AvatarMQUnsubscribeStrategy);
                break;
            default:
                break;
        }

        strategy.setChannelHandler(channelHandler);
        strategy.setHookConsumer(hookConsumer);
        strategy.setHookProducer(hookProducer);
        strategy.messageDispatch(request, response);
    }
}
