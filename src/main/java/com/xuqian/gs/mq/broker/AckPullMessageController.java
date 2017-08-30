package com.xuqian.gs.mq.broker;

import com.xuqian.gs.mq.core.AckTaskQueue;
import com.xuqian.gs.mq.core.ChannelCache;
import com.xuqian.gs.mq.core.MessageSystemConfig;
import com.xuqian.gs.mq.core.SemaphoreCache;
import com.xuqian.gs.mq.model.MessageSource;
import com.xuqian.gs.mq.model.MessageType;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.ProducerAckMessage;
import com.xuqian.gs.mq.netty.NettyUtil;
import io.netty.channel.Channel;
import java.util.concurrent.Callable;

public class AckPullMessageController implements Callable<Void> {

    private volatile boolean stoped = false;

    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }

    public Void call() {
        while (!stoped) {
            SemaphoreCache.acquire(MessageSystemConfig.AckTaskSemaphoreValue);
            ProducerAckMessage ack = AckTaskQueue.getAck();
            String requestId = ack.getAck();
            ack.setAck("");

            Channel channel = ChannelCache.findChannel(requestId);
            if (NettyUtil.validateChannel(channel)) {
                ResponseMessage response = new ResponseMessage();
                response.setMsgId(requestId);
                response.setMsgSource(MessageSource.AvatarMQBroker);
                response.setMsgType(MessageType.AvatarMQProducerAck);
                response.setMsgParams(ack);

                channel.writeAndFlush(response);
            }
        }
        return null;
    }
}
