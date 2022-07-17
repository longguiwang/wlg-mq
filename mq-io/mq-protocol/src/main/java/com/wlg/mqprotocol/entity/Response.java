package com.wlg.mqprotocol.entity;

import com.wlg.mqprotocol.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: Longgui Wang
 * @Date: 7/13/22
 * @Description: Response that wraps the response data
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -20220713L;

    private Integer result;
    private String operation;
    private String reqType;
    private Integer reqId;
    private boolean success = true;
    private T data;

    public static Response<Void> typeError(){
        Response<Void> resp = new Response<>();
        resp.setResult(ResultEnum.ParameterWrongType.getCode());
        resp.setSuccess(false);
        return resp;
    }

    public static Response<Void> topicNotExistsError(){
        Response<Void> resp = new Response<>();
        resp.setResult(ResultEnum.TopicNotExists.getCode());
        resp.setSuccess(false);
        return resp;
    }

    public static Response<Void> error(ResultEnum resultEnum){
        Response<Void> resp = new Response<>();
        resp.setResult(ResultEnum.ParameterWrongType.getCode());
        resp.setSuccess(false);
        return resp;
    }
}
