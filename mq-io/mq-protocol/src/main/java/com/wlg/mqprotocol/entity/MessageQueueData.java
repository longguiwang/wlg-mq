package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: MessageQueueData
 */
@Data
public class MessageQueueData implements Serializable {
    private static final long serialVersionUID = -20220714L;

    private String topic;
    private String consumerKey;
    private Integer queueSize;
    private Map<Integer,Long> offSetMap;
}
