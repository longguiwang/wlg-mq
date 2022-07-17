package com.wlg.mqcommon.config;

import com.wlg.mqcommon.settings.Settings;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Topic Config
 */
public class TopicConfig {
    private String topicName;
    private int messageQueueSize = Settings.DEFAULT_MESSAGE_QUEUE_SIZE;

    public static TopicConfig getDefault(String topicName){
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setTopicName(topicName);
        topicConfig.setMessageQueueSize(Settings.DEFAULT_MESSAGE_QUEUE_SIZE);
        return topicConfig;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getMessageQueueSize() {
        return messageQueueSize;
    }

    public void setMessageQueueSize(int messageQueueSize) {
        this.messageQueueSize = messageQueueSize;
    }
}
