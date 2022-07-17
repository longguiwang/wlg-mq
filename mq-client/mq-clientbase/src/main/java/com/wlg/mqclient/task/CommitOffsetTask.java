package com.wlg.mqclient.task;

import com.wlg.mqclient.entity.ConsumerMessageQueue;
import com.wlg.mqclient.register.ConsumerRegister;
import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.SyncOffsetMessage;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.protocol.EncodedData;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: CommitOffsetTask
 */
@Slf4j
public class CommitOffsetTask implements Runnable{

    private final ConsumerMessageQueue consumerMessageQueue;
    private final ChannelFuture channelFuture;
    private final String consumerGroup;
    private final String topic;

    public CommitOffsetTask(ConsumerMessageQueue consumerMessageQueue) {
        this.channelFuture = ConsumerRegister.getChannelFuture();
        this.consumerMessageQueue = consumerMessageQueue;
        this.consumerGroup = consumerMessageQueue.getConsumerGroup();
        this.topic = consumerMessageQueue.getTopic();
    }

    @Override
    public void run() {
        Map<Integer, Long> offSetMap = consumerMessageQueue.getOffSetMap();
        log.info("consumerGroupL:{} ,offSetMap:{}",consumerMessageQueue.getConsumerGroup(),offSetMap);
        while (true){
            //尚未初始化完成
            if (!ConsumerRegister.isInit(this.topic,this.consumerGroup)){
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                syncIndex();
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void syncIndex() throws InterruptedException {
        EncodedData encodedData = new EncodedData();
        encodedData.setHead(Head.toHead(MessageEnum.Req));

        Request<SyncOffsetMessage> hsReq = new Request<>();

        SyncOffsetMessage syncOffsetMessage = new SyncOffsetMessage();
        syncOffsetMessage.setTopic(consumerMessageQueue.getTopic());
        syncOffsetMessage.setConsumer(consumerMessageQueue.getConsumerGroup());
        syncOffsetMessage.setOffSetMap(consumerMessageQueue.getOffSetMap());

        hsReq.setData(syncOffsetMessage);
        hsReq.setOperation(OperationEnum.CommitOffset.getOperation());
        encodedData.setData(hsReq);

        channelFuture.channel().writeAndFlush(encodedData).sync();
    }
}