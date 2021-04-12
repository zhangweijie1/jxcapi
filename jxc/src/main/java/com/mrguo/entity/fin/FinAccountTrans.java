package com.mrguo.entity.fin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName
 * @Description 账户转账
 * @author 郭成兴
 * @date 2020/3/21 8:00 AM
 * @updater 郭成兴
 * @updatedate 2020/3/21 8:00 AM
 */
@Data
@Table(name = "fin_account_trans")
public class FinAccountTrans {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 转出账户
     */
    @NotNull(message = "转出账户不能为空")
    @Column(name = "account_out")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountOut;

    /**
     * 转入账户
     */
    @NotNull(message = "转入账户不能为空")
    @Column(name = "account_in")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountIn;

    /**
     * 转出日期
     */
    @NotNull(message = "转出日期不能为空")
    @Column(name = "trans_time_out")
    private Date transTimeOut;

    /**
     * 转入日期
     */
    @NotNull(message = "转入日期不能为空")
    @Column(name = "trans_time_in")
    private Date transTimeIn;

    /**
     * 金额
     */
    @NotNull(message = "转账金额不能为空")
    private BigDecimal amount;

    /**
     * 手续费
     */
    @Column(name = "procedure_amount")
    private BigDecimal procedureAmount;

    /**
     * 手续费支付方
     */
    @Column(name = "procedure_user")
    private String procedureUser;

    /**
     * 经手人Id
     */
    @NotNull(message = "经手人不能为空")
    @Column(name = "hand_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long handUserId;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "business_time")
    private Date businessTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否作废
     */
    @Column(name = "is_cancle")
    private String isCancle;

    @Transient
    private String isNotCancle;

    @Transient
    @Column(name="account_name_in")
    private String accountNameIn;

    @Transient
    @Column(name="account_name_out")
    private String accountNameOut;

    @Transient
    @Column(name="hand_user_name")
    private String handUserName;

    @Transient
    @Column(name="create_user_name")
    private String createUserName;

    @Transient
    private String keywords;
}