package com.wlg.mqclient.entity;

import lombok.Data;

import java.util.StringJoiner;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: SendMessageResult
 */
@Data
public class SendMessageResult {

    private volatile Integer messageResult;
    private Boolean sendDone;
    private  String msgId;
    private String respDesc;

    @Override
    public String toString() {
        return new StringJoiner(", ", SendMessageResult.class.getSimpleName() + "[", "]")
                .add("messageResult=" + messageResult)
                .add("sendDone=" + sendDone)
                .add("msgId='" + msgId + "'")
                .add("respDesc='" + respDesc + "'")
                .toString();
    }
}
