package com.sysu.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyConsumerHandler extends ChannelInboundHandlerAdapter {
    private Object message = null;

    public Object getResponse() {
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.message = msg;
    }
}
