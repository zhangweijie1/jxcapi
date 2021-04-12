package com.mrguo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/10/39:05 AM
 * @updater 郭成兴
 * @updatedate 2019/10/39:05 AM
 */
public class GlobalInterceptor implements HandlerInterceptor {

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Autowired
    private ResponseUtils responseUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
