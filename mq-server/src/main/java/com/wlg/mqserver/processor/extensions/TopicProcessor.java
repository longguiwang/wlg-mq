package com.wlg.mqserver.processor.extensions;

import com.wlg.mqprotocol.entity.MessageQueueData;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.entity.Topic;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.enums.ResultEnum;
import com.wlg.mqserver.data.TopicListener;
import com.wlg.mqserver.data.TopicManager;
import com.wlg.mqserver.processor.BaseProcessor;
import com.wlg.mqstorage.data.QueueOffsetStorage;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: TopicProcessor
 */
public class TopicProcessor extends BaseProcessor {
    @Override
    public Response<?> process(Request request) {
        if(request.getData() instanceof Topic){
            Topic data = (Topic)request.getData();
            MessageQueueData messageQueueData = new MessageQueueData();

            TopicListener topicListener = TopicManager.getTopicListener(data.getTopic());

            messageQueueData.setTopic(data.getTopic());
            messageQueueData.setConsumerKey(data.getConsumerKey());
            messageQueueData.setQueueSize(topicListener.getQueueSize());
            messageQueueData.setOffSetMap(QueueOffsetStorage.getOffSetMap(data.getTopic(),data.getConsumerGroup()));

            Response<MessageQueueData> resp = new Response<>();
            resp.setData(messageQueueData);
            resp.setOperation(OperationEnum.Response.getOperation());
            resp.setResult(ResultEnum.SendOK.getCode());
            return resp;
        }
        return null;
    }
}
