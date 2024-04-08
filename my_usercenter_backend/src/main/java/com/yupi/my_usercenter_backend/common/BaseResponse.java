package com.yupi.my_usercenter_backend.common;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.io.Serializable;

/**
 * 通用返回类
 */
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;
    /**
     * 数据
     */
    private T data;
    /**
     * 信息
     */
    private String message;
    /**
     * 描述
     */
    private String description;

    public BaseResponse(int code,T data, String message, String description){
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code,T data, String message){
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data){
        this(code, data, "", "");
    }

    /**
     * 异常通用返回接口
     * @param errorCode
     */
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }




}
