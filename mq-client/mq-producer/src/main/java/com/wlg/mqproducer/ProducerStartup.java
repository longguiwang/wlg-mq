package com.wlg.mqproducer;

import com.wlg.mqclient.ClientReactor;
import com.wlg.mqclient.entity.SendMessageResult;
import com.wlg.mqclient.producer.MessageProducer;
import com.wlg.mqprotocol.entity.SendMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Longgui Wang
 * @Date: 7/14/22
 * @Description: ProducerStartup
 */
@Slf4j
public class ProducerStartup {

    public static void main(String[] args) throws InterruptedException {
        start0();
    }

    private static void start0() throws InterruptedException{
        String start = "\n" +
                "==================================================================================================================================\n" +
                "=  ====  ====  ==  =========      ========  =====  ===      ========       ======================  ===============================\n" +
                "=  ====  ====  ==  ========   ==   =======   ===   ==  ====  =======  ====  =====================  ===============================\n" +
                "=  ====  ====  ==  ========  ====  =======  =   =  ==  ====  =======  ====  =====================  ===============================\n" +
                "=  ====  ====  ==  ========  =============  == ==  ==  ====  =======  ====  ==  =   ====   ======  ==  =  ===   ====   ===  =   ==\n" +
                "=   ==    ==  ===  ========  =============  =====  ==  ====  =======       ===    =  ==     ===    ==  =  ==  =  ==  =  ==    =  =\n" +
                "==  ==    ==  ===  ========  ===   =======  =====  ==  ====  =======  ========  =======  =  ==  =  ==  =  ==  =====     ==  ======\n" +
                "==  ==    ==  ===  ========  ====  =======  =====  ==  =  =  =======  ========  =======  =  ==  =  ==  =  ==  =====  =====  ======\n" +
                "===    ==    ====  ========   ==   =======  =====  ==  ==    =======  ========  =======  =  ==  =  ==  =  ==  =  ==  =  ==  ======\n" +
                "====  ====  =====        ===      ========  =====  ===      ========  ========  ========   ====    ===    ===   ====   ===  ======\n" +
                "==================================================================================================================================";
        log.info(start);

        //Netty Client
        ClientReactor clientReactor = new ClientReactor("127.0.0.1", 9001);
        //Start Netty Client
        clientReactor.start();

        MessageProducer.setChannelFuture(clientReactor.getChannelFuture());



        for (int i=0;i<100;i++){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setTopic("TopicB");
            sendMessage.setTag("tagB");
            sendMessage.setBody("Message---"+i);
            try{
                SendMessageResult b = MessageProducer.sendMsg(sendMessage);
                if (!Integer.valueOf(200).equals(b.getMessageResult())){
                    log.error("Message sent failed : "+b);
                }else{
                    log.info("Message sent : "+b);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(5000L);
        }

        for (int i=0;i<100;i++){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setTopic("TopicA");
            sendMessage.setTag("tagA");
            sendMessage.setBody("Message---"+i);
            try {
                SendMessageResult a = MessageProducer.sendMsg(sendMessage);

                if (!Integer.valueOf(200).equals(a.getMessageResult())) {
                    log.error("Message sent failed : " + a);
                } else {
                    log.info("Message sent : " + a);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(5000L);
        }
    }
}
