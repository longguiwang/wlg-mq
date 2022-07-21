package com.wlg.mqclient.producer;

import com.alibaba.fastjson.JSON;
import com.wlg.mqclient.entity.SendMessageResult;
import com.wlg.mqclient.handler.ClientHandler;
import com.wlg.mqcommon.settings.Settings;
import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.protocol.EncodedData;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: MessageProducer
 */
@Slf4j
public class MessageProducer {
    private static ChannelFuture channelFuture;

    private static AtomicInteger reqId = new AtomicInteger(0);

    public static void setChannelFuture(ChannelFuture channelFuture) {
        MessageProducer.channelFuture = channelFuture;
    }

    public static SendMessageResult sendMsg(SendMessage sendMessage){
        SendMessageResult sendMessageResult = null;
        try{
            EncodedData encodedData = new EncodedData();
            encodedData.setHead(Head.toHead(MessageEnum.Req));
            Request<SendMessage> request = new Request<>();
            request.setData(sendMessage);
            request.setOperation(OperationEnum.SendMessage.getOperation());
            request.setReqId(reqId.decrementAndGet());
            encodedData.setData(request);

            MessageProducer.channelFuture
                    .channel()
                    .writeAndFlush(encodedData);

            long nanosTimeout = TimeUnit.SECONDS.toNanos(Settings.DEFAULT_TIMEOUT_IN_SECONDS);
            final long deadline = System.nanoTime() + nanosTimeout;
            ClientHandler handler = (ClientHandler) MessageProducer.channelFuture.channel().pipeline().last();
            sendMessageResult = handler.getSendMessageResult();
            //sendMessageResult = new SendMessageResult();
            nanosTimeout = deadline - System.nanoTime();
            if(nanosTimeout<0){
                sendMessageResult.setRespDesc("Message send timeout");
                sendMessageResult.setMessageResult(-1);
                log.warn("Message send timeout , request is {}", JSON.toJSON(request));
            }

            return sendMessageResult;
        }catch (Exception e) {
            sendMessageResult = new SendMessageResult();
            sendMessageResult.setMessageResult(-2);
            sendMessageResult.setSendDone(false);
            e.printStackTrace();
        }
        return sendMessageResult;
    }
}
