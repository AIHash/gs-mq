package com.xuqian.gs.mq.broker;

import com.xuqian.gs.mq.consumer.ConsumerContext;
import com.xuqian.gs.mq.model.RemoteChannelData;
import com.xuqian.gs.mq.model.SubscriptionData;
import com.xuqian.gs.mq.msg.SubscribeMessage;

public class ConsumerMessageHook implements ConsumerMessageListener {

    public ConsumerMessageHook() {

    }

    public void hookConsumerMessage(SubscribeMessage request, RemoteChannelData channel) {

        System.out.println("receive subcript info groupid:" + request.getClusterId() + " topic:" + request.getTopic() + " clientId:" + channel.getClientId());

        SubscriptionData subscript = new SubscriptionData();

        subscript.setTopic(request.getTopic());
        channel.setSubcript(subscript);

        ConsumerContext.addClusters(request.getClusterId(), channel);
    }
}
