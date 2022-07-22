package com.wlg.mqstorage.data;

import com.wlg.mqprotocol.entity.PullMessage;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqstorage.config.StorageConfig;
import com.wlg.mqstorage.fuse.MessageFuse;
import com.wlg.mqstorage.persistence.FileOperation;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageStorage
 */
@Slf4j
public class MessageStorage {
    /**
     * store message
     * @param sendMessage
     * @return
     */
    public static MessageFuse saveMessage(SendMessage sendMessage){
        try {
            synchronized (MessageStorage.class){
                return FileOperation.save(getMessageStorage(sendMessage.getTopic()),sendMessage);
            }
        } catch (IOException | InterruptedException e) {
            log.error("save file error",e);
        }
        return null;
    }

    public static List<PullMessage> readMessages(String topic, List<MessageFuse> messageFuses){
        return FileOperation.readMessages(getMessageStorage(topic),messageFuses);
    }

    public static List<MessageFuse> readMessageFuse(long offset, int size, String topic){
        return FileOperation.readMessageFuse(StorageConfig.MessagePath + "mq_1",offset,size,topic);
    }

    private static String getMessageStorage(String topic){
        return StorageConfig.MessagePath + topic + StorageConfig.Message + StorageConfig.MessageFile;
    }
}
