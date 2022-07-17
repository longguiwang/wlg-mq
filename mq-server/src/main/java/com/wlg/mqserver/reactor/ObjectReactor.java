package com.wlg.mqserver.reactor;

import com.wlg.mqserver.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: description
 */
public class ObjectReactor {

    private final int port;
    public ObjectReactor(int port ) {
        this.port = port;
    }

    public void start() {
        try {
            start0();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start0() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        //TODO
        channelFuture.channel().eventLoop().scheduleWithFixedDelay(()->{
            //scheduled task
        },1, 3L,TimeUnit.SECONDS);
        channelFuture.channel().closeFuture().sync();
    }
}
