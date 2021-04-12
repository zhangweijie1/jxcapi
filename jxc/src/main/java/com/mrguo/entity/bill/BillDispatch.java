package com.mrguo.entity.bill;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * @ClassName BillDispatch
 * @Description 调拨单实体
 * @author 郭成兴
 * @date 2020/5/10 10:21 PM
 * @updater 郭成兴
 * @updatedate 2020/5/10 10:21 PM
 */
@Data
@Table(name = "t_bill_dispatch")
public class BillDispatch {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 主bill表
     */
    @Column(name = "bill_master_id")
    private Long billMasterId;

    /**
     * 仓库ID
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 进出库方向（0出，1进）
     */
    private String direction;

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
     * 欠款额度
     */
    @Column(name = "amount_debt")
    private BigDecimal amountDebt;

    /**
     * 欠款总额
     */
    @Column(name = "amount_deb_total")
    private BigDecimal amountDebTotal;

    /**
     * 其他费用
     */
    @Column(name = "amount_other")
    private BigDecimal amountOther;

    /**
     * 优惠额度
     */
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    /**
     * 结算账户
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 转化量
     */
    @Column(name = "change_quantity")
    private BigDecimal changeQuantity;

    /**
     * 返回量
     */
    @Column(name = "return_quantity")
    private BigDecimal returnQuantity;

    /**
     * 一个票据的商品名
     */
    @Column(name = "goods_namestr")
    private String goodsNamestr;

    /**
     * 经手人Id
     */
    @Column(name = "hand_user_id")
    private Long handUserId;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 修改人Id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 备注
     */
    private String remarks;

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
}