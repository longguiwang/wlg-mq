package com.wlg.mqclient.initializer;

import com.wlg.mqclient.handler.ClientHandler;
import com.wlg.mqclient.handler.ReconnectHandler;
import com.wlg.mqprotocol.decode.LengthObjectDecode;
import com.wlg.mqprotocol.encode.LengthObjectEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: ClientChannelInitializer
 */
public class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthObjectEncode());
        pipeline.addLast(new LengthObjectDecode());
        pipeline.addLast(new ReconnectHandler());
        pipeline.addLast(new ClientHandler());
    }
}
