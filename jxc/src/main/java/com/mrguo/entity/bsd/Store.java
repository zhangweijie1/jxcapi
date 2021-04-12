package com.mrguo.entity.bsd;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName Store
 * @Description 仓库实体
 * @date 2019/11/13 3:06 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 3:06 PM
 */

@Data
@Table(name = "bsd_store")
public class Store {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 仓库编号
     */
    private String code;

    /**
     * 仓库名
     */
    private String name;

    /**
     * 关键字用于查询
     */
    @Transient
    private String keywords;

    /**
     * 仓库地址
     */
    private String address;

    private String contacter;

    private String phone;

    private String email;

    @Column(name = "postal_code")
    private String postalCode;

    private String status;

    private String remark;

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
     * 是否锁定，1：锁定，0：不锁定
     */
    @Column(name = "is_lock")
    private String isLock;

    /**
     * 是否默认
     */
    @Column(name = "is_default")
    private String isDefault;
}