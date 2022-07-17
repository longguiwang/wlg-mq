package com.wlg.mqclient.entity;

import com.wlg.mqprotocol.entity.PullMessage;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: AbstractConsumer
 */
public abstract class AbstractConsumer {

    protected abstract boolean consumeMessage(PullMessage pullMessage);

    public boolean consume(PullMessage pullMessage){
        //前置处理
        boolean b = consumeMessage(pullMessage);
        //后置处理
        return b;
    }
}
