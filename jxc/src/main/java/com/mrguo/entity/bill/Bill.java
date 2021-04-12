package com.mrguo.entity.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mrguo.validation.bill.SaleAdd;
import com.mrguo.validation.bill.SaleReturnAdd;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName Bill
 * @Description 单据类
 * @date 2019/9/1210:58 AM
 * @updater 郭成兴
 * @updatedate 2019/9/1210:58 AM
 */
@Data
@Table(name = "t_bill")
public class Bill {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 仓库Id
     */
    @NotNull(message = "仓库不能为空", groups = {SaleAdd.class, SaleReturnAdd.class})
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id")
    private Long storeId;

    @Transient
    @Column(name = "store_name")
    private String storeName;

    /**
     * 单据号
     */
    @NotBlank(message = "单据编号不能为空")
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 单据类型
     */
    @Column(name = "bill_cat")
    private String billCat;

    @Column(name = "bill_cat_name")
    private String billCatName;

    /**
     * 进出库方向（0出，1进）
     */
    private String direction;

    /**
     * 关联票据
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "bill_relation_id")
    private Long billRelationId;

    @Column(name = "bill_relation_no")
    private String billRelationNo;

    /**
     * 应付金额
     */
    @NotNull(message = "应付金额不能为空")
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 优惠金额
     */
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private String discountValue;

    /**
     * 欠款金额
     */
    @Column(name = "amount_debt")
    private BigDecimal amountDebt;

    /**
     * 欠款金额
     */
    @Column(name = "amount_deb_total")
    private BigDecimal amountDebtTotal;

    /**
     * 其他费用
     */
    @Column(name = "amount_other")
    private BigDecimal amountOther;

    /**
     * 结算账户名称
     */
    @NotNull(message = "结算账户不能为空", groups = {SaleAdd.class, SaleReturnAdd.class})
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "account_id")
    private Long accountId;

    @Transient
    @Column(name = "account_name")
    private String accountName;

    @Column(name = "goods_namestr")
    private String goodsNamestr;

    /**
     * 往来单位ID
     */
    @NotNull(message = "往来单位不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "comego_id")
    private Long comegoId;

    /**
     * 往来单位名称
     */
    @Column(name = "comego_name")
    private String comegoName;

    @Transient
    @Column(name = "customer_name")
    private String customerName;

    @Transient
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 创建人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "create_user_id")
    private Long createUserId;

    @Transient
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 经手人
     */
    @NotNull(message = "经手人不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "hand_user_id")
    private Long handUserId;

    @Transient
    @Column(name = "hand_user_name")
    private String handUserName;

    /**
     * 更新人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "update_user_id")
    private Long updateUserId;

    @Transient
    @Column(name = "update_user_name")
    private String updateUserName;

    /**
     * 业务时间
     */
    @NotNull(message = "业务日期不能为空")
    @Column(name = "business_time")
    private Date businessTime;

    /**
     * 是否作废
     */
    @Column(name = "is_cancle")
    private String isCancle;

    /**
     * 是否关闭
     */
    @Column(name = "is_close")
    private String isClose;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * 审核状态
     */
    @Column(name = "audit_status")
    private String auditStatus;

    /**
     * 审核人
     */
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 总计成本
     */
    @Transient
    @Column(name = "amount_cost")
    private BigDecimal amountCost;

    @Transient
    @Column(name = "audit_user_name")
    private String auditUserName;

    /**
     * 起始时间 搜索用到
     */
    @Transient
    private String cBeginTime;
    @Transient
    private String cEndTime;
    @Transient
    private String bBeginTime;
    @Transient
    private String bEndTime;
    @Transient
    private String keywords;
    @Transient
    private Long billStockId;

    @Transient
    @Column(name = "quantity")
    private BigDecimal quantity;

    /**
     * 商品数量
     */
    @Transient
    @Column(name = "goods_quantity")
    private BigDecimal goodsQuantity;
    @Transient
    @Column(name = "change_quantity")
    private BigDecimal changeQuantity;
    @Transient
    @Column(name = "change_quantity2")
    private BigDecimal changeQuantity2;
    @Transient
    @Column(name = "return_quantity")
    private BigDecimal returnQuantity;
}
