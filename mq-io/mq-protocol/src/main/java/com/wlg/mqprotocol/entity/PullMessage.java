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
public class PullMessage implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private String msgId;
    private String topic;
    private String tag;
    private String key;
    private String body;
    private Long index;

    @Override
    public String toString() {
        return new StringJoiner(", ", PullMessage.class.getSimpleName() + "[", "]")
                .add("msgId='" + msgId + "'")
                .add("topic='" + topic + "'")
                .add("tag='" + tag + "'")
                .add("key='" + key + "'")
                .add("body='" + body + "'")
                .add("index=" + index)
                .toString();
    }
}
