package com.mrguo.entity.bsd;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author 郭成兴
 * @ClassName Account
 * @Description 银行账户
 * @date 2019/12/18 5:05 PM
 * @updater 郭成兴
 * @updatedate 2019/12/18 5:05 PM
 */
@Data
@Table(name = "bsd_account")
public class Account {

    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 账户名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 开户银行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 银行编码
     */
    @Column(name = "bank_no")
    private String bankNo;

    /**
     * 银行账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 期初金额
     */
    @Column(name = "origin_amount")
    private BigDecimal originAmount;

    @Transient
    private String originAmountStr;
    @Transient
    private String amountStr;


    /**
     * 变化金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 状态，0-不可用，1-正常
     */
    private String status;

    @Column(name = "is_default")
    private String isDefault;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    @Transient
    private String keywords;

    @Transient
    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;

    @Transient
    private String label;

    @Transient
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;
}