package com.wlg.mqprotocol.entity;

import com.wlg.mqprotocol.enums.OperationEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Request that wraps the request data
 */
@Data
public class Request<T> implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private String operation;
    private T data;
    private Integer reqId;

    public OperationEnum getOperationEnum() {
        return OperationEnum.getByCode(operation);
    }
}
