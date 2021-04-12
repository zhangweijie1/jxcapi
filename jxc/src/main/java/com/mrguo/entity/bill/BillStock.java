package com.mrguo.entity.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "t_bill_stock")
public class BillStock {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 单据编号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 仓库Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id")
    private Long storeId;

    @Transient
    @Column(name = "store_name")
    private String storeName;

    /**
     * 单据类型(purchase采购， sale销售)
     */
    @Column(name = "bill_cat")
    private String billCat;

    @Column(name = "bill_cat_name")
    private String billCatName;

    /**
     * 应付金额
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 实付金额
     */
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 结算账户
     */
    @Column(name = "account_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 商品名
     */
    @Column(name = "goods_namestr")
    private String goodsNamestr;

    /**
     * 经手人Id
     */
    @Column(name = "hand_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long handUserId;

    @Transient
    private String handUserName;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 修改人Id
     */
    @Column(name = "update_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 往来单位ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "comego_id")
    private Long comegoId;

    /**
     * 进出库方向（0出，1进）
     */
    private String direction;

    /**
     * 往来单位名称
     */
    @Column(name = "comego_name")
    private String comegoName;

    /**
     * 关联票据
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "bill_relation_id")
    private Long billRelationId;

    @Transient
    @Column(name = "bill_relation_no")
    private String billRelationNo;

    /**
     * 进出库数量
     */
    private BigDecimal quantity;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 审核状态（0待审核，1已审批）
     */
    @Column(name = "audit_status")
    private String auditStatus;

    /**
     * 审核人
     */
    @Column(name = "audit_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long auditUserId;

    /**
     * 业务时间
     */
    @Column(name = "business_time")
    private Date businessTime;

    /**
     * 是否作废
     */
    @Column(name = "is_cancle")
    private String isCancle;

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

    /**
     * 商品数量
     */
    @Transient
    @Column(name = "goods_quantity")
    private Integer goodsQuantity;

    @Transient
    private String relationBillCat;

    @Transient
    private Long storeIdOut;
    @Transient
    private Long storeIdIn;
}