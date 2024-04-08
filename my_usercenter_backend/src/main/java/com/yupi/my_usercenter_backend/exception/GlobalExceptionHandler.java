package com.yupi.my_usercenter_backend.exception;


import com.yupi.my_usercenter_backend.common.BaseResponse;
import com.yupi.my_usercenter_backend.common.ErrorCode;
import com.yupi.my_usercenter_backend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> BusinessExceptionHandle(BusinessException e){
        log.info("business exception message" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(),e.getMessage(), e.getDescription());
    }

    /**
     * 运行时 系统异常
     * @param r
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> RuntimeExceptionHandler(RuntimeException r){
        log.info("runtime exception", r);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, r.getMessage(), "");
    }
}
