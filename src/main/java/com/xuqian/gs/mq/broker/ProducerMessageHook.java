package com.xuqian.gs.mq.broker;

import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import com.google.common.base.Predicate;
import com.xuqian.gs.mq.consumer.ConsumerClusters;
import com.xuqian.gs.mq.core.*;
import com.xuqian.gs.mq.model.MessageDispatchTask;
import com.xuqian.gs.mq.msg.Message;
import com.xuqian.gs.mq.msg.ProducerAckMessage;
import io.netty.channel.Channel;
import com.google.common.base.Joiner;
import org.apache.commons.collections.ClosureUtils;
import org.apache.commons.collections.functors.AnyPredicate;
import org.springframework.cglib.core.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ProducerMessageHook implements ProducerMessageListener {

    private List<ConsumerClusters> clustersSet = new ArrayList<ConsumerClusters>();
    private List<ConsumerClusters> focusTopicGroup = null;

    private void filterByTopic(String topic) {
        Predicate focusAllPredicate = new Predicate() {
            public boolean evaluate(Object object) {
                ConsumerClusters clusters = (ConsumerClusters) object;
                return clusters.findSubscriptionData(topic) != null;
            }
        };

        AnyPredicate any = new AnyPredicate(new Predicate[]{focusAllPredicate});

        ClosureSerializer.Closure joinClosure = new ClosureSerializer.Closure() {
            public void execute(Object input) {
                if (input instanceof ConsumerClusters) {
                    ConsumerClusters clusters = (ConsumerClusters) input;
                    clustersSet.add(clusters);
                }
            }
        };

        ClosureSerializer.Closure ignoreClosure = new ClosureSerializer.Closure() {
            public void execute(Object input) {
            }
        };

        ClosureSerializer.Closure ifClosure = ClosureUtils.ifClosure(any, joinClosure, ignoreClosure);

        CollectionUtils.forAllDo(focusTopicGroup, ifClosure);
    }

    private boolean checkClustersSet(Message msg, String requestId) {
        if (clustersSet.size() == 0) {
            System.out.println("AvatarMQ don't have match clusters!");
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setMsgId(msg.getMsgId());
            ack.setAck(requestId);
            ack.setStatus(ProducerAckMessage.SUCCESS);
            AckTaskQueue.pushAck(ack);
            SemaphoreCache.release(MessageSystemConfig.AckTaskSemaphoreValue);
            return false;
        } else {
            return true;
        }
    }

    private void dispatchTask(Message msg, String topic) {
        List<MessageDispatchTask> tasks = new ArrayList<MessageDispatchTask>();

        for (int i = 0; i < clustersSet.size(); i++) {
            MessageDispatchTask task = new MessageDispatchTask();
            task.setClusters(clustersSet.get(i).getClustersId());
            task.setTopic(topic);
            task.setMessage(msg);
            tasks.add(task);

        }

        MessageTaskQueue.getInstance().pushTask(tasks);

        for (int i = 0; i < tasks.size(); i++) {
            SemaphoreCache.release(MessageSystemConfig.NotifyTaskSemaphoreValue);
        }
    }

    private void taskAck(Message msg, String requestId) {
        try {
            Joiner joiner = Joiner.on(MessageSystemConfig.MessageDelimiter).skipNulls();
            String key = joiner.join(requestId, msg.getMsgId());
            AckMessageCache.getAckMessageCache().appendMessage(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hookProducerMessage(Message msg, String requestId, Channel channel) {

        ChannelCache.pushRequest(requestId, channel);

        String topic = msg.getTopic();

        focusTopicGroup = ConsumerContext.selectByTopic(topic);

        filterByTopic(topic);

        if (checkClustersSet(msg, requestId)) {
            dispatchTask(msg, topic);
            taskAck(msg, requestId);
            clustersSet.clear();
        } else {
            return;
        }
    }
}
