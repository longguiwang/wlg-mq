package com.wlg.mqclient.register;

import com.wlg.mqclient.config.ExecutorService;
import com.wlg.mqclient.entity.ConsumerMessageQueue;
import com.wlg.mqclient.task.CommitOffsetTask;
import com.wlg.mqclient.task.ConsumeMessageTask;
import com.wlg.mqclient.task.PullMessageTask;
import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.MessageQueueData;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Topic;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.protocol.EncodedData;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ConsumerRegister
 */
@Slf4j
public class ConsumerRegister {

    private static AtomicInteger initFlag = new AtomicInteger(0);
    private static ChannelFuture channelFuture;
    private static Map<String, ConsumerMessageQueue> consumerMessageQueueMap = new HashMap<>();
    private static Map<String, Boolean> consumerMessageQueueStatusMap = new HashMap<>();

    public static void registeredConsumer(String topic,String consumerGroup){
        log.info("registeredConsumer topic:[{}]",topic);
        ThreadPoolExecutor executor = ExecutorService.getExecutor();
        //create consumer
        ConsumerMessageQueue consumerMessageQueue = new ConsumerMessageQueue(topic,consumerGroup);
        //register to consumerMessageQueueMap
        String consumerKey = consumerKey(topic,consumerGroup);
        consumerMessageQueueMap.put(consumerKey,consumerMessageQueue);
        consumerMessageQueueStatusMap.put(consumerKey,false);
        //init consumer queue which will be used for receiving pull messages
        requestConsumerQueue(consumerKey,consumerMessageQueue);
        //register pull message task
        executor.execute(new PullMessageTask(consumerMessageQueue));
        //register consumer message task
        executor.execute(new ConsumeMessageTask(consumerMessageQueue));

        //register commit offset task
        channelFuture.channel().eventLoop().scheduleWithFixedDelay(()->{
            new Thread(new CommitOffsetTask(consumerMessageQueue)).start();
        },1, 3L, TimeUnit.SECONDS);

        log.info("registeredConsumer end consumer size :{} ",initFlag.get());
    }

    private static void requestConsumerQueue(String consumerKey, ConsumerMessageQueue queue){

        Topic topic = new Topic();
        topic.setTopic(queue.getTopic());
        topic.setConsumerGroup(queue.getConsumerGroup());
        topic.setConsumerKey(consumerKey);

        Request<Topic> request = new Request<>();
        request.setData(topic);
        request.setOperation(OperationEnum.Topic.getOperation());

        EncodedData encodedData = new EncodedData();
        encodedData.setHead(Head.toHead(MessageEnum.Req));
        encodedData.setData(request);

        try {
            channelFuture.channel().writeAndFlush(encodedData).sync();
            log.info("consumer init by req server -  topic:{}",queue.getTopic());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ConsumerMessageQueue getConsumerMessageQueue(String topic,String consumerGroup){
        return consumerMessageQueueMap.get(consumerKey(topic,consumerGroup));
    }

    public static boolean isInit(String topic, String consumerGroup){
        return consumerMessageQueueStatusMap.get(consumerKey(topic,consumerGroup));
    }

    public static void initConsumerQueue(MessageQueueData messageQueueData){
        ConsumerMessageQueue consumerMessageQueue =  consumerMessageQueueMap.get(messageQueueData.getConsumerKey());
        if (consumerMessageQueue==null){
            return;
        }
        consumerMessageQueue.initQueue(messageQueueData);
        consumerMessageQueueStatusMap.put(messageQueueData.getConsumerKey(),true);
    }

    private static String consumerKey(String topic,String consumerGroup){
        return topic+":"+consumerGroup;
    }

    public static ChannelFuture getChannelFuture() {
        return ConsumerRegister.channelFuture;
    }

    public static void setChannelFuture(ChannelFuture channelFuture) {
        ConsumerRegister.channelFuture = channelFuture;
    }

}
