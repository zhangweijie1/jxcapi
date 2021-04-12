package com.mrguo.entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "sys_user")
@Data
public class SysUser {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    /**
     * 关键字 - 查询时候用到
     */
    @Transient
    private String keywords;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型
     */
    @Column(name = "user_type_id")
    private String userTypeId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 激活时间
     */
    @Column(name = "activation_time")
    private Date activationTime;

    /**
     * 过期时间
     */
    @Column(name = "overdue_time")
    private Date overdueTime;

    /**
     * 是否激活
     */
    @Column(name = "is_activation")
    private Byte isActivation;

    /**
     * 是否冻结
     */
    @Column(name = "is_frozen")
    private Byte isFrozen;

    @Transient
    private String token;
}