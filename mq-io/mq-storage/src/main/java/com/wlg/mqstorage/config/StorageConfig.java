package com.wlg.mqstorage.config;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: StorageConfig
 */
public class StorageConfig {

    public static final String MessagePath = System.getProperty("user.home")+"/data/";

    public static final String Queue = "/queue/";

    public static final String Message = "/message/";

    public static final String MessageFile = "msgstore";

    public static String rootPath = System.getProperty("user.home")+"/wlg-mq/";

    public static final String MessageStorage = rootPath+"storage";

    public static final String MessageQueue = rootPath+"message_queue";

    public static final String ConsumerMessageQueue = rootPath+"consumer_message_queue";

}
