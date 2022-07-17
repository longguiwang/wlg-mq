package com.wlg.mqserver.strategy;

import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.error.OpError;
import com.wlg.mqprotocol.protocol.DecodedData;
import com.wlg.mqprotocol.protocol.EncodedData;
import com.wlg.mqserver.processor.BaseProcessor;
import com.wlg.mqserver.processor.extensions.CommitOffsetProcessor;
import com.wlg.mqserver.processor.extensions.PullProcessor;
import com.wlg.mqserver.processor.extensions.SendMessageProcessor;
import com.wlg.mqserver.processor.extensions.TopicProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageStrategy
 */
@Slf4j
public class MessageStrategy {
    private static final Map<OperationEnum, BaseProcessor<?>> enumExecutorMap ;

    static {
        enumExecutorMap = new HashMap<>();
        enumExecutorMap.put(OperationEnum.SendMessage,new SendMessageProcessor());
        enumExecutorMap.put(OperationEnum.Pull,new PullProcessor());
        enumExecutorMap.put(OperationEnum.CommitOffset,new CommitOffsetProcessor());
        enumExecutorMap.put(OperationEnum.Topic,new TopicProcessor());
        log.info("Init MessageStrategy");
    }

    public static EncodedData process(DecodedData decodedData){
        Request<?> request = (Request<?>) decodedData.getData();

        OperationEnum operationEnum = request.getOperationEnum();
        if (operationEnum==null){
            Response<?> parameterWrongType = OpError.ParameterWrongType;
            parameterWrongType.setReqType(request.getOperation());
            parameterWrongType.setReqId(request.getReqId());
            return EncodedData.resp(parameterWrongType);
        }

        BaseProcessor<?> baseExecutor = enumExecutorMap.get(operationEnum);
        return baseExecutor.processRequest(request);
    }
}
