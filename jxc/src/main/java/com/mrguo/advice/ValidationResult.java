package com.mrguo.advice;

import lombok.Data;

/**
 * @ClassName ValidationResult
 * @Description 实体校验结果实体
 * @Author 沈晨延
 * @Date 2018-01-10 10:35
 * Version 1.0
 **/
@Data
public class ValidationResult {

    private String field;

    private String message;

    private String objectName;

    @Override
    public String toString() {
        return message;
    }
}
