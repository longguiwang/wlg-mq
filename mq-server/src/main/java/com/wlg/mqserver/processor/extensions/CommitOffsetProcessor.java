package com.wlg.mqserver.processor.extensions;

import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.entity.SyncOffsetMessage;
import com.wlg.mqprotocol.enums.OperationEnum;
import com.wlg.mqprotocol.enums.ResultEnum;
import com.wlg.mqserver.processor.BaseProcessor;
import com.wlg.mqstorage.data.QueueOffsetStorage;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: CommitOffsetProcessor
 */
@Slf4j
public class CommitOffsetProcessor extends BaseProcessor {
    @Override
    public Response<?> process(Request request) {
        if(request.getData() instanceof SyncOffsetMessage){
            SyncOffsetMessage syncOffsetMessage = (SyncOffsetMessage) request.getData();
            QueueOffsetStorage.saveConsumer(syncOffsetMessage.getTopic(),syncOffsetMessage.getConsumer(),syncOffsetMessage.getOffSetMap());
            log.info("CommitOffsetExecutor sync offset:{}",syncOffsetMessage.getOffSetMap());
            Response<String> resp = new Response<>();
            resp.setData("OK");
            resp.setOperation(OperationEnum.CommitOffset.getOperation());
            resp.setResult(ResultEnum.SendOK.getCode());
            return resp;
        }
        return null;
    }
}
