package com.mrguo.dto.report;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 利润报表收入
 * @date 2020/7/189:57 AM
 * @updater 郭成兴
 * @updatedate 2020/7/189:57 AM
 */

@Data
public class ProfitRece {

    /**
     * 销售金额
     */
    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    /**
     * 成本金额
     */
    @Column(name = "amount_cost")
    private BigDecimal amountCost;

    /**
     * 销售毛利
     */
    @Column(name = "sale_profit")
    private String saleProfit;

    /**
     * 销售其他
     */
    @Column(name = "sale_other")
    private String saleOther;

    /**
     * 销售税额
     */
    @Column(name = "sale_tax")
    private String saleTax;

    /**
     * 日常收入
     */
    @Column(name = "daily_in")
    private String dailyIn;

    /**
     * 盘盈
     */
    @Column(name = "inventory")
    private String inventory;

    /**
     * 付款优惠
     */
    @Column(name = "pay_discount")
    private String payDiscount;

    /**
     * 销售退货其他
     */
    @Column(name = "purchase_return_other")
    private BigDecimal purchaseReturnOther;


}
