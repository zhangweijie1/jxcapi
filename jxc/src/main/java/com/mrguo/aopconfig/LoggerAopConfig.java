package com.mrguo.aopconfig;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhangxh
 * @Date: 2018-11-12 09:46
 * @Description:
 */
@Aspect
@Configuration
public class LoggerAopConfig {

    private static final Logger log = LoggerFactory.getLogger(LoggerAopConfig.class);

    @Pointcut("execution(public * com.mrguo.controller.*.*(..))")
    public void aopLog() {}

    @Before("aopLog()")
    public void invokeBefore(JoinPoint point) {
        String realClassName = getRealClassName(point);
        System.out.println("调用-----" + realClassName + " 执行 " + getMethodName(point) + " 方法之前");
        log.info("调用-----" + realClassName + " 执行 " + getMethodName(point) + " 方法之前");
    }

    @After("aopLog()")
    public void after(JoinPoint point) {
        String realClassName = getRealClassName(point);
        log.info("调用-----" + realClassName + " 执行 " + getMethodName(point) + " 方法之后");
    }

    /**
     * 后置异常通知
     *
     * @param jp
     */
    @AfterThrowing(value = "execution(* com.mrguo.controller.*.*(..)))",
            throwing = "e")
    public void throwss(JoinPoint jp, Exception e) {
        String realClassName = getRealClassName(jp);
        log.error("\r\n<====================================================================================================================>"
                + "\r\n异常方法" + realClassName + "." + getMethodName(jp)
                + " \r\n异常如下:" + e.getMessage());
    }


    /**
     * 获取被代理对象的真实类全名
     *
     * @param point 连接点对象
     * @return 类全名
     */
    private String getRealClassName(JoinPoint point) {
        return point.getTarget().getClass().getName();
    }

    /**
     * 获取代理执行的方法名
     *
     * @param point 连接点对象
     * @return 调用方法名
     */
    private String getMethodName(JoinPoint point) {
        return point.getSignature().getName();
    }
}
