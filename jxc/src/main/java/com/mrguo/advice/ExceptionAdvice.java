package com.mrguo.advice;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.interceptor.ResponseUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.piccolo.util.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ExceptionControllerAdvice
 * @Description 处理实体验证异常信息
 * @Author 郭成兴
 * @Date 2018-01-07 15:44
 * Version 1.0
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @Autowired
    private ResponseUtils responseUtils;

    /**
     * 自定义异常
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(CustomsException.class)
    public Result customsException(CustomsException ex) {
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result expiredJwtException(ExpiredJwtException ex) {
        return Result.fail("登录失效");
    }

    /**
     * 业务异常，一般是出现了系统业务逻辑错误，本不该发生此类错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result businessException(BusinessException ex) {
        log.error("业务异常感知 >>> 信息: {}", (Object) ex.getStackTrace());
        return Result.fail("业务异常,联系管理员", ex.getMessage());
    }

    /**
     * Sql语法不正确异常
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public Result badSqlGrammarException(BadSqlGrammarException ex) {
        String message = "SQL语法不正确";
        log.error(message + "，" + "信息: {}", (Object) ex.getStackTrace());
        ex.printStackTrace();
        return Result.fail(message + ": " + ex.getMessage());
    }

    /**
     * 主键重复
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result duplicateKeyException(DuplicateKeyException ex) {
        return Result.fail("主键ID重复: " + ex.getMessage());
    }

    /**
     * redis异常
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(JedisConnectionException.class)
    public Result duplicateKeyException(JedisConnectionException ex) {
        log.error("Redis连接异常" + "，" + "信息: {}", (Object) ex.getStackTrace());
        return Result.fail("Redis连接异常", ex.getMessage());
    }

    /**
     * controller实体校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError error : errors) {
            FieldError fieldError = (FieldError) error;
            ValidationResult validationResult = new ValidationResult();
            validationResult.setMessage(fieldError.getDefaultMessage());
            errorMessage.append(validationResult.toString()).append(", ");
        }
        return Result.fail("数据验证有误", errorMessage.toString());
    }

    @ExceptionHandler(Exception.class)
    public Result bindExceptionHandler(Exception ex) throws IOException {
        ex.printStackTrace();
        log.error("系统全局异常感知，信息: {}", (Object) ex.getStackTrace());
        return Result.fail("系统未知异常", "请联系管理员");
    }
}
