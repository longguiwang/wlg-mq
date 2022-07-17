package com.wlg.mqprotocol.protocol;

import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.enums.MessageEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: DecodedData
 */
@Data
public class DecodedData implements Serializable {
    private Head head;
    private MessageEnum msgTypeEnum;
    private Object data;

    public static DecodedData req(Request data){
        DecodedData respHsDecodeData = new DecodedData();
        respHsDecodeData.setHead(Head.toHead(MessageEnum.Resp));
        respHsDecodeData.setData(data);
        return respHsDecodeData;
    }
}
