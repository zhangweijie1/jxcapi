package com.mrguo.interfaces;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/10/39:42 AM
 * @updater 郭成兴
 * @updatedate 2019/10/39:42 AM
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excle表头的titleName
 */
@Target({ElementType.FIELD})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface ExcleName {
    //使用的时候 @SecurityInterface(value="xxx")
    String value() default "";
}
