package com.liyaoyun.usercentre.exception;


import com.liyaoyun.usercentre.common.BaseResponse;
import com.liyaoyun.usercentre.common.ErrorCode;
import com.liyaoyun.usercentre.common.Resultutils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 使返回给前端的信息更安全,不能将报错的异常信息返回
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse BusinessExceptionHandler(BusinessException e){
        log.error("businessexceptionhander",e);
        return Resultutils.error(e.getCode(),e.getMessage(),e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse BusinessExceptionHandler(RuntimeException e){
        log.error("runtimeexception",e);
        return Resultutils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }

}
