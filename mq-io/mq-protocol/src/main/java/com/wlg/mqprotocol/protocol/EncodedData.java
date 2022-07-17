package com.wlg.mqprotocol.protocol;

import com.wlg.mqprotocol.entity.Head;
import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.enums.MessageEnum;
import com.wlg.mqprotocol.utils.ObjectByteUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: EncodedData
 */
@Data
public class EncodedData {

    private byte[] head;
    private byte[] data;

    public void setHead(Head head) {
        this.head = ObjectByteUtils.toByteArray(head);
    }

    public void setData(Serializable data) {
        this.data = ObjectByteUtils.toByteArray(data);
    }

    public int getHeadLength(){
        return head.length;
    }

    public int getDataLength(){
        return data.length;
    }

    public int getLength(){
        return 4 * 2 + getDataLength() + getHeadLength();
    }


    public static EncodedData resp(Response data){
        EncodedData respHsDecodeData = new EncodedData();
        respHsDecodeData.setHead(Head.toHead(MessageEnum.Resp));
        respHsDecodeData.setData(data);
        return respHsDecodeData;
    }

    public static EncodedData typeError(){
        EncodedData respHsDecodeData = new EncodedData();
        respHsDecodeData.setHead(Head.toHead(MessageEnum.Resp));
        respHsDecodeData.setData(Response.typeError());
        return respHsDecodeData;
    }
}
