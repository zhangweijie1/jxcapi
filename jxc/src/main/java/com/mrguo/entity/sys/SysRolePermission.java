package com.mrguo.entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "sys_role_permission")
@Data
public class SysRolePermission {
    @Column(name = "role_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @Column(name = "permission_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String permissionId;
}