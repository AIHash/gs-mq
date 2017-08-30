package com.xuqian.gs.mq.netty;

import io.netty.channel.ChannelHandlerContext;

public interface MessageEventHandler {

    void handleMessage(ChannelHandlerContext ctx, Object msg);

}
