package com.wlg.mqserver.processor.extensions;

import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.entity.SendMessage;
import com.wlg.mqprotocol.utils.MessageIdUtils;
import com.wlg.mqserver.processor.BaseProcessor;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: SendMessageProcessor
 */
public class SendMessageProcessor extends BaseProcessor {
    @Override
    public Response<?> process(Request request) {
        if (request.getData() instanceof SendMessage){
            SendMessage sendMessage = (SendMessage) request.getData();
            sendMessage.setMsgId(MessageIdUtils.newMsgId(sendMessage.getTopic(),request.getReqId().toString()));
            Response<?> response = messageStore.saveMessage(sendMessage);
            response.setReqId(request.getReqId());
            return response;
        }
        return null;
    }
}
