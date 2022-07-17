package com.wlg.mqprotocol.utils;

import java.lang.management.ManagementFactory;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageIdUtils
 */
public class MessageIdUtils {

    public static String newMsgId(String topic,String reqId){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String s = name.split("@")[0];

        return topic+"-"+s+"-"+reqId+"-"+System.nanoTime();
    }
}
