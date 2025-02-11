package com.sakura.exception;

import com.sakura.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: sakura
 * @date: 2024/10/2 23:01
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SakuraException extends RuntimeException {
    // 错误状态码
    private Integer code ;
    // 错误消息
    private String message ;

    private ResultCodeEnum resultCodeEnum ;     // 封装错误状态码和错误消息

    public SakuraException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum ;
        this.code = resultCodeEnum.getCode() ;
        this.message = resultCodeEnum.getMessage();
    }

    public SakuraException(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }
}
