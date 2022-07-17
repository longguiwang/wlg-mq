package com.wlg.mqserver.handler;

import com.wlg.mqprotocol.protocol.DecodedData;
import com.wlg.mqprotocol.protocol.EncodedData;
import com.wlg.mqserver.strategy.MessageStrategy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Server Handler
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object decoded) throws Exception {
        if (decoded==null){
            log.warn("decoded object is null");
            return;
        }
        DecodedData decodedData = (DecodedData)decoded;
        EncodedData encodedData = MessageStrategy.process(decodedData);
        ctx.channel().writeAndFlush(encodedData).sync();
    }
}
