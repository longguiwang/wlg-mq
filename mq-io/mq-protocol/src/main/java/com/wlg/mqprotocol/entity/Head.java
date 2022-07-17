package com.wlg.mqprotocol.entity;

import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.utils.ObjectByteUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Head
 */
@Data
public class Head implements Serializable {
    private static final long serialVersionUID = -20220713L;

    /**
     * @see MessageEnum
     * msgType：Req and Resp
     */
    private String msgType;

    public MessageEnum getMsgTypeEnum() {
        return MessageEnum.getByCode(msgType);
    }

    public static Head toHead(byte[] headData){
        return (Head) ObjectByteUtils.toObject(headData);
    }

    public static Head toHead(MessageEnum messageEnum){
        Head head = new Head();
        head.setMsgType(messageEnum.getCode());
        return head;
    }
}
