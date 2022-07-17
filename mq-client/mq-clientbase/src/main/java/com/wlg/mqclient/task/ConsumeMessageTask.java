package com.wlg.mqclient.task;

import com.wlg.mqclient.config.ExecutorService;
import com.wlg.mqclient.manage.ConsumerManager;
import com.wlg.mqclient.register.ConsumerRegister;
import com.wlg.mqclient.entity.ConsumerMessageQueue;
import com.wlg.mqprotocol.entity.PullMessage;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ConsumeMessageTask
 */
public class ConsumeMessageTask implements Runnable{


    private final ConsumerMessageQueue consumerMessageQueue;
    private final String consumerGroup;

    public ConsumeMessageTask(ConsumerMessageQueue consumerMessageQueue) {
        this.consumerGroup = consumerMessageQueue.getConsumerGroup();
        this.consumerMessageQueue = consumerMessageQueue;
    }

    @Override
    public void run() {
        do {
            Map<Integer, ConcurrentLinkedQueue<PullMessage>> queueMap = consumerMessageQueue.getQueueMap();
            queueMap.forEach((queueId,queue)->{
                PullMessage pullMessage = queue.poll();
                if (pullMessage != null) {
                    boolean consumer = ConsumerManager.consume(consumerGroup,pullMessage);
                    if (consumer) {
                        consumerMessageQueue.confirmOffset(queueId,pullMessage);
                    }
                }
            });
        } while (!Thread.interrupted());
    }



}