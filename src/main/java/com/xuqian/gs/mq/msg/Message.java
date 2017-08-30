package com.xuqian.gs.mq.msg;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Message extends BaseMessage implements Serializable {

    private String msgId;
    private String topic;
    private byte[] body;
    private long timeStamp;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, new String[]{"body"});
    }

    public int hashCode() {
        return new HashCodeBuilder(11, 23).append(msgId).toHashCode();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && Message.class.isAssignableFrom(obj.getClass())) {
            Message msg = (Message) obj;
            result = new EqualsBuilder().append(topic, msg.getTopic()).append(body, msg.getBody())
                    .isEquals();
        }
        return result;
    }
}
