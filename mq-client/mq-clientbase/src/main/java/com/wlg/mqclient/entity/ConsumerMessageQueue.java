package com.wlg.mqclient.entity;

import com.wlg.mqprotocol.entity.MessageQueueData;
import com.wlg.mqprotocol.entity.PullMessage;
import com.wlg.mqprotocol.entity.PullMessageResp;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ConsumerMessageQueue
 */
@Getter
public class ConsumerMessageQueue {
    private final Map<Integer, ConcurrentLinkedQueue<PullMessage>> queueMap = new ConcurrentHashMap<>();

    private final Map<Integer,Long> offSetMap = new ConcurrentHashMap<>();

    private final Map<Integer,Long> lastMessageMap = new ConcurrentHashMap<>();

    private final String topic;

    private final String  consumerGroup;

    public ConsumerMessageQueue(String topic ,String consumerGroup) {
        this.topic = topic;
        this.consumerGroup = consumerGroup;
    }

    public void initQueue(MessageQueueData messageQueueData){
        Integer queueSize = messageQueueData.getQueueSize();
        for (int i=0;i<queueSize;i++){
            queueMap.put(i,new ConcurrentLinkedQueue<>());
        }
        Map<Integer, Long> serverOffSetMap = messageQueueData.getOffSetMap();
        if (serverOffSetMap!=null&&serverOffSetMap.size()>0){
            offSetMap.putAll(serverOffSetMap);
            lastMessageMap.putAll(serverOffSetMap);
        }
    }

    public void addMessage(PullMessageResp pullMessageResp){
        if (pullMessageResp == null || pullMessageResp.getPullMessages() == null || pullMessageResp.getPullMessages().isEmpty()){
            return;
        }
        ConcurrentLinkedQueue<PullMessage> concurrentLinkedQueue = queueMap.get(pullMessageResp.getQueueId());
        if (concurrentLinkedQueue==null){
            return;
        }
        lastMessageMap.put(pullMessageResp.getQueueId(),pullMessageResp.getLastIndex());
        concurrentLinkedQueue.addAll(pullMessageResp.getPullMessages());
    }

    public void confirmOffset(Integer queueId,PullMessage pullMessage)  {
        offSetMap.put(queueId,pullMessage.getIndex());
    }

}
