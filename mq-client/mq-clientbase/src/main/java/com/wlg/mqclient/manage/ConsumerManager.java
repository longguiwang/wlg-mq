package com.wlg.mqclient.manage;

import com.wlg.mqclient.entity.AbstractConsumer;
import com.wlg.mqclient.register.ConsumerRegister;
import com.wlg.mqprotocol.entity.PullMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ConsumerManager
 */
@Slf4j
public class ConsumerManager {
    private static final Map<String, Map<String, AbstractConsumer>>  allConsumerMap = new HashMap<>();

    public static void initConsumer(String consumerGroup, Map<String,AbstractConsumer> topicConsumerMap){
        log.info("initConsumer start");
        allConsumerMap.put(consumerGroup,topicConsumerMap);
        String[] topics = new String[topicConsumerMap.size()];
        topicConsumerMap.keySet().toArray(topics);
        //init consumer
        init(consumerGroup,topics);
        log.info("initConsumer end");
    }

    public static boolean consume(String consumerGroup, PullMessage pullMessage){

        Map<String, AbstractConsumer> consumerMap = allConsumerMap.get(consumerGroup);
        if (consumerMap==null){
            log.error("ConsumerGroup:{} Not Exists Consumer",pullMessage.getTopic());
            return false;
        }
        AbstractConsumer abstractConsumer = consumerMap.get(pullMessage.getTopic());
        if (abstractConsumer==null){
            log.error("Topic:{} Not Exists Consumer",pullMessage.getTopic());
            return false;
        }
        return abstractConsumer.consume(pullMessage);
    }

    private static void init(String consumerGroup, String[] topics){
        //register consumer
        for (String topic : topics) {
            ConsumerRegister.registeredConsumer(topic,consumerGroup);
        }
    }
}
