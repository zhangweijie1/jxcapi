package com.mrguo.entity.sys;

import javax.persistence.*;

@Table(name = "sys_dict")
public class SysDict {
    /**
     * 主键value
     */
    @Id
    private String code;

    private String name;

    private String parent;

    /**
     * 获取主键value
     *
     * @return code - 主键value
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置主键value
     *
     * @param code 主键value
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * @param parent
     */
    public void setParent(String parent) {
        this.parent = parent == null ? null : parent.trim();
    }
}