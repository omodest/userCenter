package com.yupi.my_usercenter_backend.common;

/**
 *错误码
 */
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求参数为空", ""),
    AUTH_ERROR(40002, "无权限", ""),
    NOT_LOGIN(40003, "未登录", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description){
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
