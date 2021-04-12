package com.mrguo.entity.sys;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "sys_card")
public class SysCard {
    /**
     * 卡号id
     */
    @Id
    private String id;

    /**
     * 0注册，1充值
     */
    private String type;

    /**
     * 使用天数
     */
    @Column(name = "use_dates")
    private Integer useDates;

    /**
     * 仓库数量
     */
    @Column(name = "store_num")
    Integer storeNum;

    /**
     * 员工数量
     */
    @Column(name = "emp_num")
    Integer empNum;

    /**
     * 是否使用
     */
    @Column(name = "is_used")
    private String isUsed;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "used_time")
    private Date usedTime;

    /**
     * 使用人
     */
    @Column(name = "used_user")
    private Long usedUser;
}