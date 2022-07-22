package com.wlg.mqclient.handler;

import com.wlg.mqclient.config.ExecutorService;
import com.wlg.mqclient.consumer.MessageConsumer;
import com.wlg.mqclient.producer.MessageProducer;
import com.wlg.mqclient.register.ConsumerRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;


/**
 * @Auther: Longgui Wang
 * @Date: 7/19/22
 * @Description: ReconnectHandler
 */
@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {
    private static Bootstrap bootstrap;
    public static void setBootstrap(Bootstrap bootstrap){
        ReconnectHandler.bootstrap = bootstrap;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Successfully established a connection to the server.");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("Channel is not active, trying to reconnect");
        while(true){
            System.out.println("Client is trying to reconnect to server");
            try{
                ChannelFuture channelFuture = ReconnectHandler.bootstrap.connect().sync();
                System.out.println("channelFuture status is "+channelFuture.isSuccess());
                if(channelFuture.isSuccess()) {
                    MessageConsumer.setChannelFuture(channelFuture);
                    MessageProducer.setChannelFuture(channelFuture);
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            Thread.sleep(3000);
        }
    }

}
