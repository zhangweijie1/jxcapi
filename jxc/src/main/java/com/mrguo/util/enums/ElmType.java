package com.mrguo.util.enums;

public enum ElmType implements CodeEnum {

    normal("0", "普通员工"),
    guide("1", "导购"),
    manager("2", "管理员");

    private String code;
    private String message;

    private ElmType(String code, String message) {
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
