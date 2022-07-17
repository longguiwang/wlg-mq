package com.wlg.mqstorage.data;

import com.alibaba.fastjson.JSON;
import com.wlg.mqstorage.config.StorageConfig;
import com.wlg.mqstorage.fuse.TopicConsumerData;
import com.wlg.mqstorage.persistence.FileOperation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: QueueOffsetStorage
 */
public class QueueOffsetStorage {

    public static void saveConsumer(String topic,String consumer, Map<Integer, Long> offSetMap)  {

        String consumerDir = StorageConfig.MessagePath + topic;
        String consumerFileName = consumerDir +"/ConsumerData";
        boolean mkdirs = new File(consumerDir).mkdirs();
        if (mkdirs){
            throw new RuntimeException("创建文件夹失败");
        }
        TopicConsumerData topicConsumerData = readConsumer(topic);
        if (topicConsumerData==null){
            topicConsumerData = new TopicConsumerData();
            topicConsumerData.setOffSetMap(new HashMap<>());
        }
        Map<String, Map<String, Long>> offSetAllMap = topicConsumerData.getOffSetMap();
        Map<String, Long> stringLongMap = offSetAllMap.get(consumer);
        if (stringLongMap==null){
            stringLongMap = new HashMap<>();
        }
        Map<String, Long> finalStringLongMap = stringLongMap;
        offSetMap.forEach((k, v)->{
            finalStringLongMap.put(k.toString(),v);
        });
        offSetAllMap.put(consumer,finalStringLongMap);

        FileOperation.saveString(consumerFileName, JSON.toJSONString(topicConsumerData));
    }

    public static Map<Integer, Long> getOffSetMap(String topic,String consumer){

        TopicConsumerData topicConsumerData = readConsumer(topic);
        if (topicConsumerData==null){
            return null;
        }
        Map<String, Map<String, Long>> offSetMap = topicConsumerData.getOffSetMap();
        if (offSetMap==null){
            return null;
        }
        Map<String, Long> stringLongMap = offSetMap.get(consumer);
        if (stringLongMap==null){
            return null;
        }
        Map<Integer, Long> data = new HashMap<>();
        stringLongMap.forEach((k,v)->{
            data.put(Integer.valueOf(k),v);
        });
        return data;
    }

    public static TopicConsumerData readConsumer(String topic){
        String consumerDir = StorageConfig.MessagePath + topic;
        String consumerFileName = consumerDir +"/ConsumerData";
        boolean mkdirs = new File(consumerDir).mkdirs();
        if (mkdirs){
            throw new RuntimeException("创建文件夹失败");
        }
        if (!Files.exists(Paths.get(consumerFileName))){
            return null;
        }

        String s = FileOperation.readString(consumerFileName);
        if (s.length()==0){
            return null;
        }

        return JSON.parseObject(s,TopicConsumerData.class);
    }
}
