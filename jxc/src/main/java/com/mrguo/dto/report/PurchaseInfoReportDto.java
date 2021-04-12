package com.mrguo.dto.report;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/125:29 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:29 PM
 */

@Data
public class PurchaseInfoReportDto {

    /**
     * 开单数量
     */
    @Column(name = "bill_count")
    private long billCount;

    /**
     * 开单金额
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 实付金额
     */
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    /**
     * 商品成本总额（成本 X 数量）
     */
    @Column(name = "price_cost_total")
    private BigDecimal priceCostTotal;

    @Column(name = "good_name")
    private String goodName;

    @Column(name = "comego_name")
    private String comegoName;
}
