package com.wlg.mqclient.handler;

import com.wlg.mqclient.entity.ConsumerMessageQueue;
import com.wlg.mqclient.entity.SendMessageResult;
import com.wlg.mqclient.processor.ClientProcessor;
import com.wlg.mqclient.register.ConsumerRegister;
import com.wlg.mqcommon.settings.Settings;
import com.wlg.mqprotocol.entity.MessageQueueData;
import com.wlg.mqprotocol.entity.PullMessageResp;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.protocol.DecodedData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: ClientHandler
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<DecodedData> {
    //final BlockingQueue<SendMessageResult> sendMessageResultQueue = new LinkedBlockingQueue<>();
    final Queue<SendMessageResult> sendMessageResultQueue = new LinkedList<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DecodedData decodedData) throws Exception {
        Response<?> data = (Response<?>) decodedData.getData();
        if (data.getData()!=null){
            //Read Pull response, put PullMessageResp to consumer message queue
            if (data.getData() instanceof PullMessageResp){
                PullMessageResp pullMessageResp = (PullMessageResp) data.getData();
                ConsumerMessageQueue queue = ConsumerRegister.getConsumerMessageQueue(pullMessageResp.getTopic(),pullMessageResp.getConsumerGroup());
                if (queue!=null){
                    queue.addMessage(pullMessageResp);
                }else{
                    log.info("not search topic queue:{}",pullMessageResp.getTopic());
                }
            }
            //Read Topic response, init consumer queue handle
            if (data.getData() instanceof MessageQueueData){
                ConsumerRegister.initConsumerQueue((MessageQueueData)data.getData());
            }
            //Read SendMessage response
            if (OperationEnum.SendMessage.getOperation().equals(data.getReqType())){
                sendMessageResultQueue.offer(ClientProcessor.processResponse(data));
            }
        }

    }

    /*
    public SendMessageResult getSendMessageResult(){
        boolean interrupted = false;
        try {
            for (;;) {
                try {
                    return sendMessageResultQueue.take();
                } catch (InterruptedException ignore) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }*/

    public SendMessageResult getSendMessageResult(){
        boolean found = false;
        long nanosTimeout = TimeUnit.SECONDS.toNanos(Settings.DEFAULT_TIMEOUT_IN_SECONDS);
        final long deadline = System.nanoTime() + nanosTimeout;
        while(!found){
            SendMessageResult sendMessageResult = sendMessageResultQueue.peek();
            if(sendMessageResult != null){
                found = true;
                sendMessageResultQueue.remove();
                return sendMessageResult;
            }
            nanosTimeout = deadline - System.nanoTime();
            if(nanosTimeout<0){
                log.error("Get SendMessageResult timeout");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
