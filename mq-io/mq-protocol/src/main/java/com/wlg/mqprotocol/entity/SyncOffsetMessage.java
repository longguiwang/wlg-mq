package com.wlg.mqprotocol.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: SyncOffsetMessage
 */
@Data
public class SyncOffsetMessage implements Serializable {
    private static final long serialVersionUID = -20220714L;

    private String consumer;
    private String topic;
    private Map<Integer, Long> offSetMap;
}
