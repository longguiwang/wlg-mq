package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: description
 */
@Data
public class SendMessage implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private String msgId;
    private String topic;
    private String tag;
    private String body;

    @Override
    public String toString() {
        return new StringJoiner(", ", SendMessage.class.getSimpleName() + "[", "]")
                .add("msgId='" + msgId + "'")
                .add("topic='" + topic + "'")
                .add("tag='" + tag + "'")
                .add("body='" + body + "'")
                .toString();
    }
}
