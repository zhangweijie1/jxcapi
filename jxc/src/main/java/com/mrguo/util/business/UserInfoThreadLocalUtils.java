package com.mrguo.util.business;

import com.mrguo.entity.sys.UserInfo;

/**
 * @ClassName: UserInfoThreadLocalUtils
 * @Description: 当前线程的用户信息
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/7 4:34 下午
 * @Copyright 南通市韶光科技有限公司
 **/
public class UserInfoThreadLocalUtils {

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<Object>() {
        @Override
        protected Object initialValue() {
            return null;
        }
    };

    public static void set(UserInfo obj) {
        THREAD_LOCAL.set(obj);
    }

    public static UserInfo get() {
        return (UserInfo) THREAD_LOCAL.get();
    }

}
