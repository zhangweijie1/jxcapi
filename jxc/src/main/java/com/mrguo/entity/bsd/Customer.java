package com.mrguo.entity.bsd;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户实体
 *
 * @author mrguo
 */
@Data
@Table(name = "bsd_customer")
@TableName(value = "bsd_customer")
public class Customer {

    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 客户名称Code
     */
    @NotNull(message = "编号不能为空")
    @Column(length = 200)
    private String code;

    /**
     * 客户名称
     */
    @Column(length = 200)
    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "分类不能为空")
    @Column(name = "cat_id")
    private String catId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "level_id")
    private Long levelId;

    @Transient
    @Column(name = "level_name")
    private String levelName;

    @Transient
    @Column(name = "cat_name")
    private String catName;

    /**
     * 联系人
     */
    @Column(length = 50)
    private String contacter;

    /**
     * 联系电话
     */
    @Column(length = 50)
    private String phone;

    /**
     * 联系地址
     */
    @Column(length = 300)
    private String address;

    @Column(name = "employee_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;

    @Transient
    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "wechat")
    private String wechat;

    @Column(name = "qq")
    private String qq;

    @Column(name = "company_tel")
    private String companyTel;

    @Column(name = "tax")
    private String tax;

    @Column(name = "email")
    private String email;

    @Column(name = "postal")
    private String postal;

    @Column(name = "is_public")
    private String isPublic;

    @Column(name = "is_default")
    private String isDefault;

    @Column(name = "is_enable")
    private String isEnable;

    /**
     * 期初欠款
     */
    @Column(name = "origin_debt")
    private BigDecimal originDebt;

    /**
     * 期初后欠款总额
     */
    private BigDecimal debt;

    /**
     * 备注
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "update_user")
    private Long updateUser;

    @Transient
    private String keywords;

    @Transient
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Transient
    @Column(name = "supplier_code")
    private String supplierCode;

    @Transient
    @Column(name = "supplier_name")
    private String supplierName;


    /**
     * 回收
     */
    @Transient
    @Column(name = "amount_rece")
    private BigDecimal amountRece;

    /**
     * 优惠
     */
    @Transient
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

}
