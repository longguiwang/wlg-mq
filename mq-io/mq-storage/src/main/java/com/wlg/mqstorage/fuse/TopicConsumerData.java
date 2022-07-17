package com.wlg.mqstorage.fuse;

import lombok.Data;

import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: TopicConsumerData
 */
@Data
public class TopicConsumerData {

    private Map<String, Map<String, Long>> offSetMap;
}
