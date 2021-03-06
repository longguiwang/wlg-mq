package com.wlg.mqclient.task;

import com.wlg.mqclient.entity.ConsumerMessageQueue;
import com.wlg.mqclient.register.ConsumerRegister;
import com.wlg.mqcommon.settings.Settings;
import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.Pull;
import com.wlg.mqprotocol.entity.PullMessage;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.protocol.EncodedData;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: PullMessageTask
 */
@Slf4j
public class PullMessageTask implements Runnable{

    private final ConsumerMessageQueue consumerMessageQueue;
    private ChannelFuture channelFuture;
    private final String consumerGroup;
    private final String topic;

    public PullMessageTask(ConsumerMessageQueue consumerMessageQueue) {
        this.channelFuture = ConsumerRegister.getChannelFuture();
        this.consumerGroup = consumerMessageQueue.getConsumerGroup();
        this.topic = consumerMessageQueue.getTopic();
        this.consumerMessageQueue = consumerMessageQueue;
    }

    @Override
    public void run() {
        log.info("pull message from topic:{} ,consumer group is:{}, channelFuture is {}",this.topic,consumerMessageQueue.getConsumerGroup(),this.channelFuture);
        while (true){
            //check if init queue is completed
            //log.info("check if init queue is completed");
            if (!ConsumerRegister.isInit(this.topic,this.consumerGroup)){
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            Map<Integer, ConcurrentLinkedQueue<PullMessage>> queueMap = consumerMessageQueue.getQueueMap();
            for (Integer queueId : queueMap.keySet()) {
                pull(queueId);
            }
        }
    }

    private void pull(Integer queueId) {
        EncodedData encodedData = new EncodedData();
        encodedData.setHead(Head.toHead(MessageEnum.Req));

        Map<Integer, Long> lastMessageMap = consumerMessageQueue.getLastMessageMap();

        Request<Pull> request = new Request<>();

        Pull pull = new Pull();
        pull.setTopic(topic);
        pull.setQueueId(queueId);
        pull.setConsumerGroup(consumerGroup);
        pull.setSize(Settings.DEFAULT_PULL_SIZE);

        if (lastMessageMap.get(queueId)==null){
            pull.setOffset(0);
        }else{
            pull.setOffset(lastMessageMap.get(queueId)+1);
        }

        request.setData(pull);
        request.setOperation(OperationEnum.Pull.getOperation());
        encodedData.setData(request);
        //if channel is inactive, retrieve the channel again from ConsumerRegister,
        // as it will reconnect to server to get new channel.
        if(!channelFuture.channel().isActive()){
            channelFuture = ConsumerRegister.getChannelFuture();
        }
        if(!channelFuture.channel().isActive()){
            log.warn("Channel is not active, skip pull message");
            return;
        }
        try {
            channelFuture.channel().writeAndFlush(encodedData).sync();
            Thread.sleep(10L);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}