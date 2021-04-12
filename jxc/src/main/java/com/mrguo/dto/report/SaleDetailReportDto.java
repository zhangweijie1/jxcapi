package com.mrguo.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售明细报表
 * @date 2020/5/125:29 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:29 PM
 */

@Data
public class SaleDetailReportDto {

    @Column(name = "bill_cat_name")
    private String billCatName;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "business_time")
    private Date businessTime;

    @Column(name = "bill_id")
    private String billId;

    @Column(name = "bill_no")
    private String billNo;

    private String direction;

    @Column(name = "sku_code")
    private String skuCode;

    @Column(name = "sku_name")
    private String skuName;

    @Column(name = "sku_cat_name")
    private String skuCatName;

    private String barcode;

    @Column(name = "comego_code")
    private String comegoCode;

    @Column(name = "comego_name")
    private String comegoName;

    @Column(name = "comego_cat_name")
    private String comegoCatName;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "unit_multi")
    private String unitMulti;

    /**
     * 送货日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Column(name = "delivery_date")
    private Date deliveryDate;

    /**
     * 数量（非基础单位）
     */
    private BigDecimal quantity;

    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 税额
     */
    @Column(name = "amount_tax")
    private BigDecimal amountTax;

    /**
     * 折扣总额
     */
    @Column(name = "amount_discount")
    private BigDecimal amountDiscount;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 单成本价
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;

    /**
     * 成本总额
     */
    @Column(name = "price_cost_total")
    private BigDecimal priceCostTotal;

    /**
     * 备注
     */
    private String remarks;
}
