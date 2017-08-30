package com.xuqian.gs.mq.producer;


import com.xuqian.gs.mq.core.AvatarMQAction;
import com.xuqian.gs.mq.model.MessageSource;
import com.xuqian.gs.mq.model.MessageType;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.Message;
import com.xuqian.gs.mq.msg.ProducerAckMessage;
import com.xuqian.gs.mq.netty.MessageProcessor;

import java.util.concurrent.atomic.AtomicLong;


public class AvatarMQProducer extends MessageProcessor implements AvatarMQAction {

    private boolean brokerConnect = false;
    private boolean running = false;
    private String brokerServerAddress;
    private String topic;
    private String defaultClusterId = "AvatarMQProducerClusters";
    private String clusterId = "";
    private AtomicLong msgId = new AtomicLong(0L);

    public AvatarMQProducer(String brokerServerAddress, String topic) {
        super(brokerServerAddress);
        this.brokerServerAddress = brokerServerAddress;
        this.topic = topic;
    }

    private ProducerAckMessage checkMode() {
        if (!brokerConnect) {
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setStatus(ProducerAckMessage.FAIL);
            return ack;
        }

        return null;
    }

    public void start() {
        super.getMessageConnectFactory().connect();
        brokerConnect = true;
        running = true;
    }

    public void init() {
        ProducerHookMessageEvent hook = new ProducerHookMessageEvent();
        hook.setBrokerConnect(brokerConnect);
        hook.setRunning(running);
        super.getMessageConnectFactory().setMessageHandle(new MessageProducerHandler(this, hook));
    }

    public ProducerAckMessage delivery(Message message) {
        if (!running || !brokerConnect) {
            return checkMode();
        }

        message.setTopic(topic);
        message.setTimeStamp(System.currentTimeMillis());

        RequestMessage request = new RequestMessage();
        request.setMsgId(String.valueOf(msgId.incrementAndGet()));
        request.setMsgParams(message);
        request.setMsgType(MessageType.AvatarMQMessage);
        request.setMsgSource(MessageSource.AvatarMQProducer);
        message.setMsgId(request.getMsgId());

        ResponseMessage response = (ResponseMessage) sendAsynMessage(request);
        if (response == null) {
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setStatus(ProducerAckMessage.FAIL);
            return ack;
        }

        ProducerAckMessage result = (ProducerAckMessage) response.getMsgParams();
        return result;
    }

    public void shutdown() {
        if (running) {
            running = false;
            super.getMessageConnectFactory().close();
            super.closeMessageConnectFactory();
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
