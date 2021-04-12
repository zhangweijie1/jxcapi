package com.mrguo.common.exception;

/**
 * @ClassName CustomsException
 * @Description 自定义异常，无需log，只要返回给用户
 * @Author 沈晨延
 * @Date 2019-01-16 11:04
 * Version 1.0
 **/
public class CustomsException extends Exception {

    public CustomsException() {
        super();
    }

    public CustomsException(String message) {
        super(message);
    }

    public CustomsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomsException(Throwable cause) {
        super(cause);
    }

    public CustomsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
