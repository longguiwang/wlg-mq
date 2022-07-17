package com.wlg.mqconsumer;

import com.wlg.mqclient.ClientReactor;
import com.wlg.mqclient.entity.AbstractConsumer;
import com.wlg.mqconsumer.test.TopicAConsumer;
import com.wlg.mqconsumer.test.TopicBConsumer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ConsumerStartup
 */
@Slf4j
public class ConsumerStartup {

    public static void main(String[] args) throws InterruptedException {
        start0();
    }

    private static void start0() throws InterruptedException{
        String start = "\n" +
                "===================================================================================================================================\n" +
                "=  ====  ====  ==  =========      ========  =====  ===      ==========     ========================================================\n" +
                "=  ====  ====  ==  ========   ==   =======   ===   ==  ====  ========  ===  =======================================================\n" +
                "=  ====  ====  ==  ========  ====  =======  =   =  ==  ====  =======  =============================================================\n" +
                "=  ====  ====  ==  ========  =============  == ==  ==  ====  =======  =========   ===  = ====   ===  =  ==  =  = ====   ===  =   ==\n" +
                "=   ==    ==  ===  ========  =============  =====  ==  ====  =======  ========     ==     ==  =  ==  =  ==        ==  =  ==    =  =\n" +
                "==  ==    ==  ===  ========  ===   =======  =====  ==  ====  =======  ========  =  ==  =  ===  ====  =  ==  =  =  ==     ==  ======\n" +
                "==  ==    ==  ===  ========  ====  =======  =====  ==  =  =  =======  ========  =  ==  =  ====  ===  =  ==  =  =  ==  =====  ======\n" +
                "===    ==    ====  ========   ==   =======  =====  ==  ==    ========  ===  ==  =  ==  =  ==  =  ==  =  ==  =  =  ==  =  ==  ======\n" +
                "====  ====  =====        ===      ========  =====  ===      ==========     ====   ===  =  ===   ====    ==  =  =  ===   ===  ======\n" +
                "===================================================================================================================================";
        log.info(start);

        //Netty Client
        ClientReactor clientReactor = new ClientReactor("127.0.0.1", 9001);
        //Start Netty Client
        clientReactor.start();

        MessageConsumer.setChannelFuture(clientReactor.getChannelFuture());

        Map<String, AbstractConsumer> consumerMap = new HashMap<>();
        consumerMap.put("TopicA",new TopicAConsumer());
        consumerMap.put("TopicB",new TopicBConsumer());

        MessageConsumer.initConsumer("TestGroup",consumerMap);
    }
}
