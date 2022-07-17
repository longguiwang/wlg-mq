package com.wlg.mqserver.bootstrap;

import com.wlg.mqserver.config.ServerConfig;
import com.wlg.mqserver.reactor.ObjectReactor;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Server side bootstrap
 */
public class MqServerBootstrap {

    public void start() {
        //start service
        new ObjectReactor(ServerConfig.Port).start();

    }
}
