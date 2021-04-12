package com.mrguo.common.entity;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/143:32 PM
 * @updater 郭成兴
 * @updatedate 2019/4/143:32 PM
 */
public class StatusCode {
    // 成功
    public static final Integer OK = 20000;
    // 失败
    public static final Integer FAIL = 20001;
    // 登录失败
    public static final Integer LOGIN_FAIL = 20002;
    // 权限不足
    public static final Integer ACCESS_ERROR = 20003;
    // 远程调用失败
    public static final Integer REMOTE_ERROR = 20004;
    // 重复操作
    public static final Integer REP_ERROR = 20005;
}
