package com.wlg.mqserver.processor;

import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.protocol.EncodedData;
import com.wlg.mqserver.data.MessageStore;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: null.java
 */
public abstract class BaseProcessor<T> {
    public static MessageStore messageStore = new MessageStore();

    public abstract Response<?> process(Request<?> request);

    public EncodedData processRequest(Request<?> request){
        if(request == null){
            return EncodedData.typeError();
        }
        Response<?> response = process(request);
        response.setReqType(request.getOperation());
        response.setReqId(request.getReqId());
        return EncodedData.resp(response);
    }
}
