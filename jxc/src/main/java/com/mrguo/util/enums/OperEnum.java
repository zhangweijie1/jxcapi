package com.mrguo.util.enums;

/**
 * @author 郭成兴
 * @ClassName BillCatEnum
 * @Description 单据类型枚举类
 * @date 2019/12/22 1:12 PM
 * @updater 郭成兴
 * @updatedate 2019/12/22 1:12 PM
 */
public enum OperEnum implements CodeEnum {
    add("add", "新增"),
    del("del", "删除"),
    update("update", "编辑"),
    query("query", "查询");

    private String code;

    private String message;

    private OperEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

