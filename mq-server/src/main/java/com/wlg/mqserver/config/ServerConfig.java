package com.wlg.mqserver.config;

import com.wlg.mqcommon.config.TopicConfig;
import com.wlg.mqcommon.settings.Settings;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: ServerConfig
 */
public class ServerConfig {

    public static int Port = Settings.DEFAULT_PORT;
    public static ConcurrentHashMap<String, TopicConfig> TopicConfig = new ConcurrentHashMap<>();
}
