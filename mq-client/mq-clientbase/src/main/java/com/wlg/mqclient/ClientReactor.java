package com.wlg.mqclient;

import com.wlg.mqclient.initializer.ClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: ClientReactor
 */
public class ClientReactor {

    private final String host;
    private final int port;
    private Bootstrap bootstrap;

    public ClientReactor(String host, int port) {
        this.host = host;
        this.port = port;
        this.bootstrap = new Bootstrap();
    }

    public void start(){
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ClientChannelInitializer());
    }

    public ChannelFuture getChannelFuture() throws InterruptedException {
        return bootstrap.connect(host, port).sync();
    }
}
