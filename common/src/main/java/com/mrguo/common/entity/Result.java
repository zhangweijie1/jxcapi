
package com.mrguo.common.entity;

import lombok.Data;

/**
 * @ClassName: Result
 * @Description: 返回结果集
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/6 4:38 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
public class Result {
    /**
     * 是否成功
     */
    private boolean flag;

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 标题
     */
    private String title;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 成功带标题
     *
     * @param title 标题
     * @return
     */
    public static Result okmsg(String title) {
        return new Result(true, 200, title, null, null);
    }

    public static Result ok() {
        return new Result(true, 200, "成功", null, null);
    }

    public static Result ok(Integer code, String message) {
        return new Result(true, code, "成功", message, null);
    }

    /**
     * 成功+数据
     *
     * @param data
     * @return
     */
    public static Result ok(Object data) {
        return new Result(true, 200, "成功", null, data);
    }

    /**
     * 成功带标题和返回数据
     *
     * @param title
     * @param data
     * @return
     */
    public static Result ok(String title, Object data) {
        return new Result(true, 200, title, null, data);
    }

    /**
     * 失败带标题
     *
     * @param title
     * @return
     */
    public static Result fail(String title) {
        return new Result(false, 500, title, null);
    }

    /**
     * 失败带标题和message
     *
     * @param title   标题
     * @param message 信息
     * @return
     */
    public static Result fail(String title, String message) {
        return new Result(false, 500, title, message);
    }

    /**
     * 失败：code，title，messahe
     *
     * @param code
     * @param title
     * @param message
     * @return
     */
    public static Result fail(Integer code, String title, String message) {
        return new Result(false, code, title, message);
    }

    public static Result fail(Integer code, String title) {
        return new Result(false, code, title, null);
    }

    public Result(boolean flag, Integer code, String title, String message) {
        this.flag = flag;
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public Result(boolean flag, Integer code, String title, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.title = title;
        this.message = message;
        this.data = data;
    }
}
