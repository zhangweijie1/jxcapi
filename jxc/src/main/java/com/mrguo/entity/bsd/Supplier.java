package com.mrguo.entity.bsd;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName Supplier
 * @Description 供应商实体
 * @date 2019/11/13 3:08 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 3:08 PM
 */

@Data
@Table(name = "bsd_supplier")
@TableName(value = "bsd_supplier")
public class Supplier {

    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 编码
     */
    @NotBlank(message = "编号不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 分类
     */
    @Column(name = "cat_id")
    private String catId;

    /**
     * 联系人
     */
    private String contacter;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    @Column(name = "origin_debt")
    private BigDecimal originDebt;

    /**
     * 当前欠款
     */
    private BigDecimal debt;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * qq号
     */
    private String qq;

    /**
     * 单位电话
     */
    @Column(name = "company_tel")
    private String companyTel;

    /**
     * 传真
     */
    private String tax;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 邮编
     */
    private String postal;

    /**
     * 关联供货商
     */
    @Column(name = "relation_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long relationId;

    /**
     * 备注
     */
    private String remarks;

    @Column(name = "is_public")
    private String isPublic;

    @Column(name = "is_default")
    private String isDefault;

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

    @Column(name = "update_user")
    private Long updateUser;

    @Transient
    @TableField(exist = false)
    private String keywords;

    @Transient
    @Column(name = "customer_id")
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @Transient
    @Column(name = "customer_code")
    @TableField(exist = false)
    private String customerCode;

    @Transient
    @TableField(exist = false)
    @Column(name = "customer_name")
    private String customerName;

    @Transient
    @Column(name = "amount_debt")
    private BigDecimal amountDebt;

    @Transient
    @Column(name = "amount_rece")
    private BigDecimal amountRece;

    @Transient
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;
}
