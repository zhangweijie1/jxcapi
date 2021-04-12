package com.mrguo.interceptor;

import com.mrguo.config.StaticConfig;
import com.mrguo.interfaces.ValiBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/10/39:05 AM
 * @updater 郭成兴
 * @updatedate 2019/10/39:05 AM
 */
public class ValiBalanceInterceptor implements HandlerInterceptor {

    @Autowired
    private StaticConfig staticConfig;

    @Autowired
    private ResponseUtils responseUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // ①:START 方法注解级拦截器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要权限
        ValiBalance methodAnnotation = method.getAnnotation(ValiBalance.class);
        if (methodAnnotation == null) {
            return true;
        }
        MethodParameter[] parameters = ((HandlerMethod) handler).getMethodParameters();
        System.out.println("========: " + parameters[0]);
        for(MethodParameter parameter: parameters){
            parameter.getParameter();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("afterCompletion");
    }
}
