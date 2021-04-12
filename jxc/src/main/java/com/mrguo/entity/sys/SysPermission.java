package com.mrguo.entity.sys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "sys_permission")
@Data
public class SysPermission {
    /**
     * id
     */
    @Id
    @Column(name = "per_id")
    private String perId;

    /**
     * api权限名/菜单文字/组件名
     */
    @Column(name = "per_name")
    private String perName;

    /**
     * api权限资源/路由路径/组件路径
     */
    @Id
    private String resource;

    /**
     * 权限父节点（0为根节点）
     */
    @Column(name = "per_parent")
    private String perParent;

    /**
     * 状态：0禁用，1启用
     */
    private Byte status;

    /**
     * 优先级，越大，同级显示的时候越靠前
     */
    private Integer priority;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "SysPermission{" +
                "perId='" + perId + '\'' +
                ", perName='" + perName + '\'' +
                ", resource='" + resource + '\'' +
                ", perParent=" + perParent +
                ", status=" + status +
                ", priority=" + priority +
                ", remark='" + remark + '\'' +
                '}';
    }
}