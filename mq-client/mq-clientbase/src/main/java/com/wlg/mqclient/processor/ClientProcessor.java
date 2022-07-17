package com.wlg.mqclient.processor;

import com.wlg.mqclient.entity.SendMessageResult;
import com.wlg.mqprotocol.entity.Response;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Longgui Wang
 * @Date: 7/15/22
 * @Description: ClientProcessor
 */
public class ClientProcessor {

    public static SendMessageResult processResponse(Response<?> data){
        SendMessageResult sendMessageResult = new SendMessageResult();//sendMessageResultMap.get(data.getReqId());
        sendMessageResult.setMsgId((String) data.getData());
        sendMessageResult.setMessageResult(data.getResult());
        sendMessageResult.setSendDone(data.isSuccess());
        sendMessageResult.setRespDesc("Message sent Successfully");
        return sendMessageResult;
    }
}
