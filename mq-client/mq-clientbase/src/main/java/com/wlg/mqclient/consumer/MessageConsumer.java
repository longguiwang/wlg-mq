package com.wlg.mqclient.consumer;

import com.wlg.mqclient.entity.AbstractConsumer;
import com.wlg.mqclient.manage.ConsumerManager;
import com.wlg.mqclient.register.ConsumerRegister;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: MessageConsumer
 */
public class MessageConsumer {

    public static void setChannelFuture(ChannelFuture channelFuture) {
        ConsumerRegister.setChannelFuture(channelFuture);
    }

    public static void initConsumer(String consumerGroup, Map<String, AbstractConsumer> topicConsumerMap){
        ConsumerManager.initConsumer(consumerGroup,topicConsumerMap);
    }
}
