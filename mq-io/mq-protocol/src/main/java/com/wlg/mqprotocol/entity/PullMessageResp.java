package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: PullMessageResp
 */
@Data
public class PullMessageResp implements Serializable {

    private static final long serialVersionUID = -20220714L;

    private List<PullMessage> pullMessages;
    private String topic;
    private String consumerGroup;
    private Integer queueId;
    private Long lastIndex;
}
