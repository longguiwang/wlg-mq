package com.wlg.mqprotocol.error;

import com.wlg.mqprotocol.entity.Response;
import com.wlg.mqprotocol.enums.ResultEnum;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: null.java
 */
public class OpError {
    public static final Response<?> ParameterWrongType = Response.error(ResultEnum.ParameterWrongType);

}
