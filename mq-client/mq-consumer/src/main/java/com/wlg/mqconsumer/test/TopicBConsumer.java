package com.wlg.mqconsumer.test;

import com.wlg.mqclient.entity.AbstractConsumer;
import com.wlg.mqprotocol.entity.PullMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicBConsumer extends AbstractConsumer {

    @Override
    protected boolean consumeMessage(PullMessage pullMessage) {

        log.info("TopicBConsumer consume:{}",pullMessage);
        return true;
    }
}
