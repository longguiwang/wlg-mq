package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: description
 */
@Data
public class Pull implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private String consumerGroup;
    private int queueId;
    private String topic;
    private int size;
    private long offset;
    private String tags;
}
