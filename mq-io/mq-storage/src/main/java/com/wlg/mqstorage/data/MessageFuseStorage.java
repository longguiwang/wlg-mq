package com.wlg.mqstorage.data;

import com.wlg.mqstorage.config.StorageConfig;
import com.wlg.mqstorage.fuse.MessageFuse;
import com.wlg.mqstorage.persistence.FileOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageFuseStorage
 */
public class MessageFuseStorage {

    public static void saveMessageQueue(Integer queueId,String topic, List<MessageFuse> data) throws IOException {
        String queueFileName = StorageConfig.MessagePath + topic +StorageConfig.Queue + "queue_" + queueId;
        new File(StorageConfig.MessagePath + topic +StorageConfig.Queue ).mkdirs();
        try {
            FileOperation.save(queueFileName, data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<MessageFuse>  readMessageQueue(Integer queueId, String topic, long index){
        String queueFileName = StorageConfig.MessagePath + topic +StorageConfig.Queue + "queue_"+ queueId;
        new File(StorageConfig.MessagePath + topic +StorageConfig.Queue ).mkdirs();
        try {
            return FileOperation.readMessageQueue(queueFileName, index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
