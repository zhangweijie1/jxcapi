package com.mrguo.annotation;

import com.mrguo.util.enums.OperEnum;

import java.lang.annotation.*;

/**
 * @author 郭成兴
 * @ClassName EmployeesOperLog
 * @Description 员工操作日志
 * @date 2020/3/274:36 PM
 * @updater 郭成兴
 * @updatedate 2020/3/274:36 PM
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmployeesOperLog {
    OperEnum oper();
}
