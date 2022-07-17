package com.wlg.mqserver.data;

import com.wlg.mqcommon.config.TopicConfig;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqstorage.fuse.MessageFuse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: TopicManager, used to store TopicListener concurrent map.
 */
public class TopicManager {

    private static final ConcurrentMap<String, TopicListener> DATA = new ConcurrentHashMap<>();

    public static void registerTopic(TopicConfig topicConfig){
        DATA.put(topicConfig.getTopicName(), new TopicListener(topicConfig));
    }

    public static TopicListener getTopicListener(String topic){
        return DATA.get(topic);
    }

    public static boolean existsTopic(SendMessage sendMessage){
        return getTopicListener(sendMessage.getTopic())!=null;
    }

}
