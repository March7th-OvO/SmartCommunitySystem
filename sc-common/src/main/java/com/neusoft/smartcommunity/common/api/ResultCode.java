package com.neusoft.smartcommunity.common.api;

/**
 * 通用错误码定义
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(0, "成功"),
    FAILED(1, "失败"),
    VALIDATE_ERROR(1001, "参数校验失败"),
    UNAUTHORIZED(1002, "未登录或登录已过期"),
    FORBIDDEN(1003, "没有权限"),
    BUSINESS_ERROR(1004, "业务异常"),
    SERVER_ERROR(1005, "系统内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

