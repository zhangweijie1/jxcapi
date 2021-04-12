package com.mrguo.common.exception;

/**
 * @ClassName CustomsException
 * @Description 业务异常，需要log记录
 * @Author 郭成兴
 * @Date 2019-01-16 11:04
 * Version 1.0
 **/
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
