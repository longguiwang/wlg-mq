package com.wlg.mqserver.processor.extensions;

import com.wlg.mqprotocol.entity.*;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.enums.ResultEnum;
import com.wlg.mqserver.data.TopicListener;
import com.wlg.mqserver.data.TopicManager;
import com.wlg.mqserver.processor.BaseProcessor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: PullProcessor
 */
public class PullProcessor extends BaseProcessor {
    @Override
    public Response<?> process(Request request) {
        if(request.getData() instanceof Pull) {
            Pull pull = (Pull)request.getData();
            TopicListener topicListener = TopicManager.getTopicListener(pull.getTopic());
            if (topicListener==null){
                return Response.typeError();
            }

            List<PullMessage> pullMessages = topicListener.pullMessage(pull);
            PullMessageResp pullMessageResp = new PullMessageResp();
            pullMessageResp.setPullMessages(pullMessages);
            pullMessageResp.setTopic(pull.getTopic());
            pullMessageResp.setQueueId(pull.getQueueId());
            pullMessageResp.setConsumerGroup(pull.getConsumerGroup());
            if (pullMessages!=null){
                Optional<Long> last = pullMessages.stream().map(PullMessage::getIndex).max(Comparator.comparing(Long::longValue));
                last.ifPresent(pullMessageResp::setLastIndex);
            }

            Response<PullMessageResp> resp = new Response<>();
            resp.setData(pullMessageResp);
            resp.setOperation(OperationEnum.Response.getOperation());
            resp.setResult(ResultEnum.SendOK.getCode());
            return resp;
        }
        return null;
    }
}
