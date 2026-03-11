package com.neusoft.smartcommunity.common.handler;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        return new Result<>(ex.getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    public Result<Void> handleValidateException(Exception ex) {
        return Result.failed(ResultCode.VALIDATE_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOtherException(Exception ex) {
        return Result.failed(ResultCode.SERVER_ERROR, ex.getMessage());
    }
}

