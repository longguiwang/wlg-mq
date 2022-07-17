package com.wlg.mqserver;

import com.wlg.mqcommon.config.TopicConfig;
import com.wlg.mqserver.bootstrap.MqServerBootstrap;
import com.wlg.mqserver.config.ServerConfig;
import com.wlg.mqserver.data.TopicManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Longgui Wang
 * @Date: 7/12/22
 * @Description: Netty Server to accept both producer and consumer sockets
 */
@Slf4j
public class ServerStartup {

    public static void main(String[] args) {
        start();
    }

    private static void start(){
        String start =
                "\n██╗    ██╗██╗      ██████╗     ███╗   ███╗ ██████╗     ███████╗███████╗██████╗ ██╗   ██╗███████╗██████╗ \n" +
                  "██║    ██║██║     ██╔════╝     ████╗ ████║██╔═══██╗    ██╔════╝██╔════╝██╔══██╗██║   ██║██╔════╝██╔══██╗\n" +
                  "██║ █╗ ██║██║     ██║  ███╗    ██╔████╔██║██║   ██║    ███████╗█████╗  ██████╔╝██║   ██║█████╗  ██████╔╝\n" +
                  "██║███╗██║██║     ██║   ██║    ██║╚██╔╝██║██║▄▄ ██║    ╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██╔══╝  ██╔══██╗\n" +
                  "╚███╔███╔╝███████╗╚██████╔╝    ██║ ╚═╝ ██║╚██████╔╝    ███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║\n" +
                  " ╚══╝╚══╝ ╚══════╝ ╚═════╝     ╚═╝     ╚═╝ ╚══▀▀═╝     ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝\n";
        log.info(start);
        registerTopics();
        //启动Reactor Server
        new MqServerBootstrap().start();
    }

    private static void registerTopics() {
        ServerConfig.TopicConfig.put("TopicA", TopicConfig.getDefault("TopicA"));
        ServerConfig.TopicConfig.put("TopicB", TopicConfig.getDefault("TopicB"));
        ServerConfig.TopicConfig.forEach((topic,config)->{
            TopicManager.registerTopic(config);
        });
    }

}
