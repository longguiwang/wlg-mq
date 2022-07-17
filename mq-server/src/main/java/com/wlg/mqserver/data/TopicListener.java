package com.wlg.mqserver.data;

import com.wlg.mqcommon.config.TopicConfig;
import com.wlg.mqprotocol.entity.Pull;
import com.wlg.mqprotocol.entity.PullMessage;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqstorage.data.MessageFuseStorage;
import com.wlg.mqstorage.data.MessageStorage;
import com.wlg.mqstorage.fuse.MessageFuse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: TopicListener, used to pull message from message queue and save message fuse to queue.
 */
public class TopicListener {
    
    private TopicConfig topicConfig;
    private volatile int queueId =  0;
    public int queueSize ;

    public TopicListener(TopicConfig topicConfig) {
        this.topicConfig = topicConfig;
        this.queueSize = topicConfig.getMessageQueueSize();
    }

    /**
     * pullMessage will get message fuse from the queue and use message fuse to get read data.
     * @param  pull
     * @return List<PullMessage>
     */
    public List<PullMessage> pullMessage(Pull pull){
        List<MessageFuse> data = MessageFuseStorage.readMessageQueue(pull.getQueueId(), pull.getTopic(), pull.getOffset());
        if (data.size()==0){
            return null;
        }
        return MessageStorage.readMessages(pull.getTopic(),data);
    }

    /**
     * addMsg2Queue will store the message fuse to queue and return true if operation is successful.
     * @param sendMessage
     * @param messageFuse
     * @return boolean
     */
    public boolean addMsg2Queue(SendMessage sendMessage, MessageFuse messageFuse){
        int queueId = getQueueId();
        try {
            MessageFuseStorage.saveMessageQueue(queueId,sendMessage.getTopic(), Collections.singletonList(messageFuse));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private synchronized int  getQueueId(){
        queueId++;
        if (queueId>10000){
            queueId = 0;
        }
        return queueId%queueSize;
    }

    public int getQueueSize() {
        return queueSize;
    }
}
