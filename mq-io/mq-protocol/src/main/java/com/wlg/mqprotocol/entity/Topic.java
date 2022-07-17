package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: description
 */
@Data
public class Topic implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private String consumerKey;
    private String consumerGroup;
    private String topic;
}
