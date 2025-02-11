package com.sakura.handler;

import com.sakura.common.Result;
import com.sakura.exception.SakuraException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/10/2 22:20
 * @description:
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return Result.error(400, msg);
    }

    @ExceptionHandler(SakuraException.class)
    @ResponseBody
    public Object handleSakuraException(SakuraException e) {
        log.error("自定义异常 -> {}", e.getResultCodeEnum().getMessage());
        return Result.error(e.getResultCodeEnum()) ;
    }

    // @ExceptionHandler(Exception.class)
    // @ResponseBody
    // public Object handleRuntimeException(Exception e){
    //     log.error("全局异常 -> {}", e.toString());
    //     return Result.build(null , 201,"服务器异常") ;
    // }
}
