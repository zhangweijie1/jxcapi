package com.mrguo.entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 员工实体
 * @date 2019/12/27 9:51 AM
 * @updater 郭成兴
 * @updatedate 2019/12/27 9:51 AM
 */
@Data
@Table(name = "sys_employee")
public class SysEmployee {
    /**
     * id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机
     */
    private String phone;

    /**
     * Email
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态：0锁定 1正常
     */
    private String status;

    /**
     * 类型（0普通员工，1导购，2管理员）
     * （导购员，只有销售单的经手人可以选导购员，其他地方无效）
     */
    @Column(name = "type")
    private String type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "remarks")
    private String remarks;

    @Transient
    private String username;
    @Transient
    @Column(name = "role_name")
    private String roleName;
    @Transient
    private String password;
    @Transient
    private String keywords;
    @Transient
    private String value;
    @Transient
    private String label;
}