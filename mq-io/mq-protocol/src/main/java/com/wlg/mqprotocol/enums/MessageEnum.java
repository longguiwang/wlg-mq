package com.wlg.mqprotocol.enums;

import com.wlg.mqprotocol.entity.Request;
import com.wlg.mqprotocol.entity.Response;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: MessageEnum
 */
public enum MessageEnum {
    Req("Req", Request.class),
    Resp("Resp", Response.class);

    private String code;
    private Class clazz;

    MessageEnum(String code, Class clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public String getCode() {
        return code;
    }

    public static MessageEnum getByCode(String code){
        for (MessageEnum value : values()) {
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
}
