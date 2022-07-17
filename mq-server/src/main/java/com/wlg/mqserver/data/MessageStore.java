package com.wlg.mqserver.data;

import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.enums.ResultEnum;
import com.wlg.mqstorage.data.MessageStorage;
import com.wlg.mqstorage.fuse.MessageFuse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageStore
 */
@Slf4j
public class MessageStore {
    
    public Response<?> saveMessage(SendMessage sendMessage){
        boolean existsTopic = TopicManager.existsTopic(sendMessage);
        if (!existsTopic){
            return Response.topicNotExistsError();
        }

        MessageFuse messageFuse = MessageStorage.saveMessage(sendMessage);
        TopicListener topicListener = TopicManager.getTopicListener(sendMessage.getTopic());
        boolean push = topicListener.addMsg2Queue(sendMessage, messageFuse);
        if (!push){
            return Response.topicNotExistsError();
        }
        log.info("saveMessage#messageFuse:{}", messageFuse);
        Response<String> resp = new Response<>();
        resp.setData(sendMessage.getMsgId());
        resp.setOperation(OperationEnum.Response.getOperation());
        resp.setResult(ResultEnum.SendOK.getCode());
        return resp;
    }
}
