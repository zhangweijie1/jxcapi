package com.mrguo.common.entity;

import lombok.Data;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/143:07 PM
 * @updater 郭成兴
 * @updatedate 2019/4/143:07 PM
 */
@Data
public class PageResut<T> {
    private Integer code;

    private Boolean flag;

    private String message;

    private PageInfo pageInfo;

    private T data;

    public static <T> PageResut ok(PageInfo pageInfo, T data){
        return new PageResut<T>(200, true, pageInfo, data, "成功!");
    }

    public static <T>PageResut fail(String message){
        return new PageResut<T>(200, false, null, null, message);
    }

    public PageResut (Integer code, Boolean flag, PageInfo pageInfo, T data, String message) {
        this.code = code;
        this.flag = flag;
        this.pageInfo = pageInfo;
        this.data = data;
        this.message = message;
    }

    public PageResut () {
        this.flag = true;
        this.code = 200;
    }
}
