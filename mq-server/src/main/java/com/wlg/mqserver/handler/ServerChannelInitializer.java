package com.wlg.mqserver.handler;

import com.wlg.mqprotocol.decode.LengthObjectDecode;
import com.wlg.mqprotocol.encode.LengthObjectEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Server ChannelInitializer
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthObjectDecode());
        pipeline.addLast(new LengthObjectEncode());
        pipeline.addLast(new ServerHandler());
    }
}
