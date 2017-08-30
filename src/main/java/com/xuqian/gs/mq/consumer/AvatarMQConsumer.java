package com.xuqian.gs.mq.consumer;

import com.google.common.base.Joiner;
import com.xuqian.gs.mq.core.AvatarMQAction;
import com.xuqian.gs.mq.core.MessageIdGenerator;
import com.xuqian.gs.mq.core.MessageSystemConfig;
import com.xuqian.gs.mq.model.MessageType;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.msg.SubscribeMessage;
import com.xuqian.gs.mq.msg.UnSubscribeMessage;
import com.xuqian.gs.mq.netty.MessageProcessor;

public class AvatarMQConsumer extends MessageProcessor implements AvatarMQAction {

    private ProducerMessageHook hook;
    private String brokerServerAddress;
    private String topic;
    private boolean subscribeMessage = false;
    private boolean running = false;
    private String defaultClusterId = "AvatarMQConsumerClusters";
    private String clusterId = "";
    private String consumerId = "";

    public AvatarMQConsumer(String brokerServerAddress, String topic, ProducerMessageHook hook) {
        super(brokerServerAddress);
        this.hook = hook;
        this.brokerServerAddress = brokerServerAddress;
        this.topic = topic;
    }

    private void unRegister() {
        RequestMessage request = new RequestMessage();
        request.setMsgType(MessageType.AvatarMQUnsubscribe);
        request.setMsgId(new MessageIdGenerator().generate());
        request.setMsgParams(new UnSubscribeMessage(consumerId));
        sendSyncMessage(request);
        super.getMessageConnectFactory().close();
        super.closeMessageConnectFactory();
        running = false;
    }

    private void register() {
        RequestMessage request = new RequestMessage();
        request.setMsgType(MessageType.AvatarMQSubscribe);
        request.setMsgId(new MessageIdGenerator().generate());

        SubscribeMessage subscript = new SubscribeMessage();
        subscript.setClusterId((clusterId.equals("") ? defaultClusterId : clusterId));
        subscript.setTopic(topic);
        subscript.setConsumerId(consumerId);

        request.setMsgParams(subscript);

        sendAsynMessage(request);
    }

    public void init() {
        super.getMessageConnectFactory().setMessageHandle(new MessageConsumerHandler(this, new ConsumerHookMessageEvent(hook)));
        Joiner joiner = Joiner.on(MessageSystemConfig.MessageDelimiter).skipNulls();
        consumerId = joiner.join((clusterId.equals("") ? defaultClusterId : clusterId), topic, new MessageIdGenerator().generate());
    }

    public void start() {
        if (isSubscribeMessage()) {
            super.getMessageConnectFactory().connect();
            register();
            running = true;
        }
    }

    public void receiveMode() {
        setSubscribeMessage(true);
    }

    public void shutdown() {
        if (running) {
            unRegister();
        }
    }

    public String getBrokerServerAddress() {
        return brokerServerAddress;
    }

    public void setBrokerServerAddress(String brokerServerAddress) {
        this.brokerServerAddress = brokerServerAddress;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isSubscribeMessage() {
        return subscribeMessage;
    }

    public void setSubscribeMessage(boolean subscribeMessage) {
        this.subscribeMessage = subscribeMessage;
    }

    public String getDefaultClusterId() {
        return defaultClusterId;
    }

    public void setDefaultClusterId(String defaultClusterId) {
        this.defaultClusterId = defaultClusterId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
