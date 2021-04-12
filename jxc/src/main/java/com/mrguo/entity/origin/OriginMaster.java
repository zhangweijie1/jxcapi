package com.mrguo.entity.origin;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "origin_master")
public class OriginMaster {
    /**
     * 主键
     */
    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "balance_time")
    private Date balanceTime;

    /**
     * 创建时间（期初时间）
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private Long createUser;

    @Transient
    private String username;
}