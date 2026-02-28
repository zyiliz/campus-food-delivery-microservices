package com.example.interceptor;

import com.example.Exception.BusinessException;
import com.example.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;

/**
 * 全局异常拦截
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    // 捕获 MethodArgumentNotValidException 异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn(e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : allErrors) {
            errorMsg.append(error.getDefaultMessage()).append(";");
        }
        return Result.fail(errorMsg.toString());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<String> handConstraintViolationException(ConstraintViolationException e) {
        logger.warn(e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder errorMsg = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            errorMsg.append(violation.getMessage()).append(";");
        }
        return Result.fail(errorMsg.toString());
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, IllegalArgumentException.class})
    public Result<String> handleInternalServerError(Exception e) {
        logger.error("全局异常捕获: ", e);
        return Result.fail("内部服务错误，请稍后重试");
    }

    // 捕获400 Bad Request异常
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public Result<String> handleBadRequestException(Exception e) {
        logger.warn(e);
        return Result.fail("请求参数错误或类型不匹配");
    }

    // 捕获405 请求方法错误异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.warn(e);
        return Result.fail("请求方法错误");
    }

    // 捕获404 Not Found异常
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<String> handleNotFoundException(NoHandlerFoundException e) {
        logger.warn(e);
        return Result.fail("无法找到请求接口");
    }

    // 你的自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        logger.warn("业务异常: {}", e.getMessage());
        // 直接返回异常里的 message，不要屏蔽成“内部错误”
        return Result.fail(e.getMessage());
    }


}
