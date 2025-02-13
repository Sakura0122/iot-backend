package com.sakura.common;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    LOGIN_AUTH(208, "未登录"),
    PERMISSION(209, "没有权限"),
    ARGUMENT_VALID_ERROR(210, "参数校验异常"),

    ACCOUNT_EXIST(213, "账号已存在"),
    ACCOUNT_ERROR(214, "账号不正确"),
    PASSWORD_ERROR(215, "密码不正确"),

    PHONE_CODE_ERROR(215, "手机验证码不正确"),
    ACCOUNT_STOP( 216, "账号已停用"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    VALIDATECODE_ERROR(218 , "验证码错误"),
    NONE_FILE(220, "请选择文件"),
    PARAM_ERROR(400, "入参错误"),
    ;

    private Integer code;      // 业务状态码
    private String message;    // 响应消息

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
