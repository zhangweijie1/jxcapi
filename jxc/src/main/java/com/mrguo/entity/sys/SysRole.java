package com.mrguo.entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "sys_role")
@Data
public class SysRole {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "id")
    private Long id;

    /**
     * 角色名字
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 状态：0禁用，1启用
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;
}