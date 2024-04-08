package com.yupi.my_usercenter_backend.common;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功返回
     * @param data
     * @return
     * @param <T>
     */
    public static<T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0, data, "ok","");
    }

    /**
     * 错误 （调用BaseResponse中一个参数的构造方法）
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    /**
     * 错误（调用BaseResponse中三个参数的构造方法）
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description){
        return new BaseResponse<>(code,null, message, description);
    }

    /**
     * 错误
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description){
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 错误
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String description){
        return new BaseResponse<>(errorCode.getCode(), null, "",description);
    }


}
