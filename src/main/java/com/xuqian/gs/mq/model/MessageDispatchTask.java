
package com.xuqian.gs.mq.model;

import java.io.Serializable;

import com.xuqian.gs.mq.msg.Message;
import org.apache.commons.lang3.builder.EqualsBuilder;


public class MessageDispatchTask implements Serializable {

    private String clusters;

    private String topic;

    private Message message;

    public String getClusters() {
        return clusters;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && MessageDispatchTask.class.isAssignableFrom(obj.getClass())) {
            MessageDispatchTask task = (MessageDispatchTask) obj;
            result = new EqualsBuilder().append(clusters, task.getClusters()).append(topic, task.getTopic()).append(message, task.getMessage())
                    .isEquals();
        }
        return result;
    }
}
