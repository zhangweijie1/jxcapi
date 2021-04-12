package com.mrguo.common.entity;

/**
 * 信息公共命名类
 *
 * @author xieds
 * @date 2018/11/5 10:10
 * @updater xieds
 * @updatedate 2018/11/5 10:10
 */
public class MessageInfo {

    public static String CONTACT_USER = "请联系管理员!";
    public static String USER_ISNOT_EXSIT = "此用户不存在";
    public static String USER_IS_EXSIT = "此用户已存在";
    public static String REGISTER_OK = "注册成功";
    public static String REGISTER_FAIL = "注册失败";
    public static String REGISTER_ERROR = "注册异常";

    public static String LOGIN_SYS_ERROR = "登录异常";
    public static String LOGIN_EXPIRED = "登录已过期";
    public static String LOGIN_SYS_TOO_MANY = "该用户重复,"+CONTACT_USER;
    public static String LOGIN_FAIL = "用户名或密码错误";

    public static String ACCESS_ERROR = "权限不足";

    public static String SUCCESS = "成功！";
    public static String FAIL = "失败！";
    public static String QUERY_SUCCESS = "查询成功！";
    public static String QUERY_FAILURE = "查询失败！";
    public static String SAVE_SUCCESS = "保存成功！";
    public static String SAVE_FAILURE = "保存失败！";
    public static String UPDATE_SUCCESS = "修改成功！";
    public static String UPDATE_FAILURE = "修改失败！";
    public static String DELETE_SUCCESS = "删除成功！";
    public static String DELETE_FAILURE = "删除失败！";
}
