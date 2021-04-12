package com.mrguo.entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_user_role")
@Data
public class SysUserRole {
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
}