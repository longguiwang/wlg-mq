package com.wlg.mqprotocol.enums;

import com.wlg.mqprotocol.entity.*;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: description
 */

public enum OperationEnum {

    /**
     * send message
     */
    SendMessage("SendMessage", SendMessage.class),

    /**
     * pull message
     */
    Pull("Pull", Pull.class),

    /**
     * pull topic
     */
    Topic("Topic", Pull.class),

    /**
     * confirm commit offset
     */
    CommitOffset("CommitOffset", SendMessage.class),

    /**
     * return response
     */
    Response("Response", null),

    ;

    private String operation;
    private Class clazz;

    OperationEnum(String operation, Class clazz) {
        this.operation = operation;
        this.clazz = clazz;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public static OperationEnum getByCode(String code){
        for (OperationEnum value : values()) {
            if (value.getOperation().equals(code)){
                return value;
            }
        }
        return null;
    }
}
