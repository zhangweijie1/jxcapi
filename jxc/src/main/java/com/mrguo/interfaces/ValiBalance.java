package com.mrguo.interfaces;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 校验结账日期
 * @date 2019/10/39:42 AM
 * @updater 郭成兴
 * @updatedate 2019/10/39:42 AM
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要登录验证的Controller的方法上使用此注解
 */
@Target({ElementType.METHOD})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface ValiBalance {
}
