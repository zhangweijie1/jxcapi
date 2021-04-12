package com.mrguo.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/125:29 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:29 PM
 */

@Data
public class SaleReportDto {

    /**
     * 商品SKU_ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "sku_id")
    private Long skuId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "comego_id")
    private Long comegoId;

    /**
     * 商品标题
     */
    private String name;

    private String code;

    @Column(name = "cat_name")
    private String catName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "business_time")
    private Date businessTime;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "comego_name")
    private String comegoName;

    @Column(name = "comego_cat_name")
    private String comegoCatName;

    private BigDecimal quantity;

    @Column(name = "sale_type")
    private String saleType;

    private String direction;

    /**
     * 单位Name
     */
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 规格
     */
    private String specs;

    /**
     * 开单数量
     */
    @Column(name = "bill_count")
    private long billCount;

    /**
     * 退货单数量
     */
    @Column(name = "return_bill_count")
    private long returnBillCount;

    /**
     * 销售量
     */
    @Column(name = "qty_in_base")
    private BigDecimal qtyInBase;

    /**
     * 退货量
     */
    @Column(name = "return_qty_in_base")
    private BigDecimal returnQtyInBase;

    /**
     * 销售开单金额
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 退货额
     */
    @Column(name = "return_amount_payable")
    private BigDecimal returnAmountPayable;

    /**
     * 实付金额
     */
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 优惠额（折扣额）
     */
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    /**
     * 其他金额
     */
    @Column(name = "amount_other")
    private BigDecimal amountOther;

    /**
     * 退货其他金额
     */
    @Column(name = "return_amount_other")
    private BigDecimal returnAmountOther;

    /**
     * 商品成本
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;

    /**
     * 商品成本总额（成本 X 数量）
     */
    @Column(name = "price_cost_total")
    private BigDecimal priceCostTotal;

    /**
     * 日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
}
