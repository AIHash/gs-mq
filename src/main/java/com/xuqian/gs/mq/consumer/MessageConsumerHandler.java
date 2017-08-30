package com.xuqian.gs.mq.consumer;

import com.xuqian.gs.mq.core.HookMessageEvent;
import com.xuqian.gs.mq.model.MessageSource;
import com.xuqian.gs.mq.model.MessageType;
import com.xuqian.gs.mq.model.RequestMessage;
import com.xuqian.gs.mq.model.ResponseMessage;
import com.xuqian.gs.mq.msg.ConsumerAckMessage;
import com.xuqian.gs.mq.netty.MessageEventWrapper;
import com.xuqian.gs.mq.netty.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;


public class MessageConsumerHandler extends MessageEventWrapper<Object> {

    private String key;

    public MessageConsumerHandler(MessageProcessor processor) {
        this(processor, null);
        super.setWrapper(this);
    }

    public MessageConsumerHandler(MessageProcessor processor, HookMessageEvent hook) {
        super(processor, hook);
        super.setWrapper(this);
    }

    public void beforeMessage(Object msg) {
        key = ((ResponseMessage) msg).getMsgId();
    }

    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        if (!factory.traceInvoker(key) && hook != null) {

            ResponseMessage message = (ResponseMessage) msg;
            ConsumerAckMessage result = (ConsumerAckMessage) hook.callBackMessage(message);
            if (result != null) {
                RequestMessage request = new RequestMessage();
                request.setMsgId(message.getMsgId());
                request.setMsgSource(MessageSource.AvatarMQConsumer);
                request.setMsgType(MessageType.AvatarMQMessage);
                request.setMsgParams(result);

                ctx.writeAndFlush(request);
            }
        }
    }
}
