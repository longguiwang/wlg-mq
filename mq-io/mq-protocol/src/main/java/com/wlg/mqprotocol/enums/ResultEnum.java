package com.wlg.mqprotocol.enums;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: ResultEnum
 */
public enum ResultEnum {

    SendOK(200,"SendOK"),
    ParameterWrongType(1000,"Wrong Parameter type"),
    TopicNotExists(1001,"Topic Not Exists type"),;

    private Integer code;
    private String dec;

    ResultEnum(Integer code, String dec) {
        this.code = code;
        this.dec = dec;
    }

    public Integer getCode() {
        return code;
    }

    public String getDec() {
        return dec;
    }
}
