package com.mrguo.entity.bill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * @author 郭成兴
 * @ClassName BillDetail
 * @Description 单据明细表（商品）
 * @date 2019/11/13 1:58 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 1:58 PM
 */

@Data
@Table(name = "t_bill_detail")
public class BillDetail {

    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 单据Id
     */
    @Column(name = "bill_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    /**
     * 明细关联的其他bill的id
     */
    @Column(name = "bill_relation_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billRelationId;

    /**
     * 商品sku_id
     */
    @Column(name = "sku_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    @Transient
    @Column(name = "specs")
    private String specs;

    @Transient
    @Column(name = "is_enable_specs")
    private String isEnableSpecs;

    @Column(name = "direction")
    private String direction;

    /**
     * 成本价
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;


    /**
     * 商品编码
     */
    private String code;

    /**
     * 商品名
     */
    private String name;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 该商品金额
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 变动数量(区分正负) 用于退换货，转订单数量变化
     */
    @Column(name = "change_quantity")
    private BigDecimal changeQuantity;

    /**
     * 继续转化（如：借出，借出->出库->转销售）
     */
    @Column(name = "change_quantity2")
    private BigDecimal changeQuantity2;

    /**
     * 退回数量
     */
    @Column(name = "return_quantity")
    private BigDecimal returnQuantity;

    /**
     * 默认单价
     */
    @Column(name = "price_def")
    private BigDecimal priceDef;

    /**
     * 折扣（%）
     */
    @Column(name = "discount")
    private BigDecimal discount;

    /**
     * 折后单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal total;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    /**
     * 计量单位Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "unit_id")
    private Long unitId;

    /**
     * 计量单位相对基础单位的倍数
     */
    @Column(name = "unit_multi")
    private BigDecimal unitMulti;

    /**
     * 单行商品备注
     */
    private String remarks;

    /**
     * 原始数量（不作为数据库字段，用于传输，检测是否转化完成）
     */
    @Transient
    private BigDecimal originQuantity;

    @Transient
    @Column(name = "bill_no")
    private String billNo;

    @Transient
    @Column(name = "store_id")
    private Long storeId;
    @Transient
    @Column(name = "unit_name")
    private String unitName;
    @Transient
    private BigDecimal qtyInBaseUnit;
    @Transient
    @TableField(exist = false)
    private BigDecimal totalCostPrice;
    @Transient
    @Column(name = "price_str")
    private String priceStr;
    @Transient
    @Column(name = "unit_id_str")
    private String unitIdStr;
    @Transient
    @Column(name = "unit_name_str")
    private String unitNameStr;
    @Transient
    @Column(name = "unit_multi_str")
    private String unitMultiStr;
    @Transient
    @Column(name = "unit_isbase_str")
    private String unitIsbaseStr;
    @Transient
    @Column(name = "is_cancle")
    private String isCancle;
}