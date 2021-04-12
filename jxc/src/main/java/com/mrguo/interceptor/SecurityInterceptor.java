package com.mrguo.interceptor;

import com.mrguo.common.entity.MessageInfo;
import com.mrguo.config.StaticConfig;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysUserServiceImpl;
import com.mrguo.util.business.JwtUtil;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/10/39:05 AM
 * @updater 郭成兴
 * @updatedate 2019/10/39:05 AM
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private StaticConfig staticConfig;

    private int ACCESS_ERROR = 403;
    private int LOGIN_EXPIRED = 401;

    @Autowired
    private ResponseUtils responseUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!Authentication(request, response, handler)) {
            return false;
        }
        return Authorization(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("afterCompletion");
    }

    /**
     * 认证
     *
     * @return
     */
    private Boolean Authentication(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String url = request.getServletPath();
        if(url.contains("webjars") || url.contains("swagger-resources") || url.contains("/v2/api-docs")){
            return true;
        }
        List<String> apiWhiteList = staticConfig.getApiWhiteList();
        if (apiWhiteList.contains(url)) {
            return true;
        }
        try {
            String token = request.getHeader("token");
            if (StringUtils.isBlank(token)){
                responseUtils.setResponse(200, "token不能为空!");
                return false;
            }
            // token 得到的数据
            Map<String,Object> map = parseJwt(token);
            Long userId = (Long) map.get("userId");
            request.setAttribute("userId", userId);
            // redis得到的数据
            UserInfo userInfo = getUserInfoByUserId(userId, request);
            userInfo.setUserId(userId);
            request.setAttribute("userInfo", userInfo);
            UserInfoThreadLocalUtils.set(userInfo);
            return true;
        } catch (ExpiredJwtException ex) {
            responseUtils.setResponse(LOGIN_EXPIRED, MessageInfo.LOGIN_EXPIRED);
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 授权
     * 接口权限值A: -> 如： 添加用户
     * 获取request中的JWT-TOKEN（没有TOKEN，说明没有登录），获取权限值Array
     * 如果A在权限值Array中，则说明拥有权限
     * 不存在则没有权限
     *
     * @return
     */
    private Boolean Authorization(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // ①:START 方法注解级拦截器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要权限
        ApiPermission methodAnnotation = method.getAnnotation(ApiPermission.class);
        if (methodAnnotation == null) {
            return true;
        }
        // 需要的权限
        String token = request.getHeader("token");
        String targetPermission = methodAnnotation.value();
        ArrayList<String> hasPermissions = new ArrayList<>();
        try {
            Map<String,Object> map = parseJwt(token);
            Long userId = (Long) map.get("userId");
            hasPermissions.addAll(getPermissions(userId, request));
        } catch (Exception ex) {
            responseUtils.setResponse(LOGIN_EXPIRED, MessageInfo.LOGIN_EXPIRED);
            ex.printStackTrace();
            return false;
        }
//        System.out.println("需要的权限:" + targetPermission);
//        System.out.println("拥有的权限:" + hasPermissions);
        return comparePermission(targetPermission, hasPermissions);
    }

    private Boolean comparePermission(String targetPermission, ArrayList<String> permissionSource) throws IOException {
        if (permissionSource.contains(targetPermission)) {
            return true;
        } else {
            // 权限不足
            responseUtils.setResponse(ACCESS_ERROR, MessageInfo.ACCESS_ERROR);
            return false;
        }
    }

    /**
     * 解析得到： userId
     *
     * @param token
     * @return
     */
    private Map<String,Object> parseJwt(String token) {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Long.valueOf(userId));
        return map;
    }

    private UserInfo getUserInfoByUserId(Long userId, HttpServletRequest request) {
        // 获取权限
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        SysUserServiceImpl userService = applicationContext.getBean(SysUserServiceImpl.class);
        return userService.getUserInfoByUserId(userId);
    }

    private List<String> getPermissions(Long userId, HttpServletRequest request) {
        // 获取权限
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        SysUserServiceImpl userService = applicationContext.getBean(SysUserServiceImpl.class);
        return userService.getPermissionsByUserId(userId);
    }
}
