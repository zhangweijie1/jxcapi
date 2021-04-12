package com.mrguo.entity.fin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@Table(name = "fin_bill")
public class FinBill {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 单据编号
     */
    @NotBlank(message = "单据编号不能为空")
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 票据类型
     */
    @Column(name = "bill_cat")
    private String billCat;

    @Column(name = "bill_cat_name")
    private String billCatName;

    /**
     * 账目类型
     */
    @Column(name = "capital_cat")
    private Long capitalCat;

    /**
     * 往来单位
     */
    @Column(name = "comego_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long comegoId;

    /**
     * 结算账户
     */
    @Column(name = "account_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 方向（1进账，0支出）
     */
    private String direction;

    /**
     * 应付款项
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 实付金额
     */
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 优惠额度
     */
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    /**
     * 收回款额
     */
    @Column(name = "amount_rece")
    private BigDecimal amountRece;

    /**
     * 期初欠款
     */
    @Transient
    @Column(name = "origin_amount")
    private String originAmount;

    /**
     * 新增欠款（业务单据等）
     */
    @Transient
    @Column(name = "amount_debt")
    private String amountDebt;

    /**
     * 合计金额
     */
    @Column(name = "amount_total")
    private BigDecimal amountTotal;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 经手人Id
     */
    @Column(name = "hand_user_id")
    private Long handUserId;

    /**
     * 业务时间(收付款时间)
     */
    @Column(name = "business_time")
    private Date businessTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
    @Column(name = "comego_code")
    private String comegoCode;

    @Column(name = "comego_name")
    private String comegoName;

    @Transient
    private String contacter;

    @Transient
    private String phone;

    @Transient
    @Column(name = "customer_code")
    private String customerCode;

    @Transient
    @Column(name = "customer_name")
    private String customerName;

    @Transient
    @Column(name = "supplier_code")
    private String supplierCode;

    @Transient
    @Column(name = "supplier_name")
    private String supplierName;

    @Transient
    @Column(name = "capital_cat_name")
    private String capitalCatName;

    @Transient
    @Column(name = "account_name")
    private String accountName;

    @Transient
    private String keywords;
}