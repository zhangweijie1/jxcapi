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
public class PurchaseSaleCompareReportDto {

    private String name;

    private String code;

    /**
     * 基本单位Name
     */
    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "qty_purchase")
    private BigDecimal qtyPurchase;

    @Column(name = "qty_purchase_return")
    private BigDecimal qtyPurchaseReturn;

    @Column(name = "qty_sale")
    private BigDecimal qtySale;

    @Column(name = "qty_sale_return")
    private BigDecimal qtySaleReturn;

    @Column(name = "amount_purchase")
    private BigDecimal amountPurchase;

    @Column(name = "amount_purchase_return")
    private BigDecimal amountPurchaseReturn;

    @Column(name = "amount_sale")
    private BigDecimal amountSale;

    @Column(name = "amount_sale_return")
    private BigDecimal amountSaleReturn;

    @Column(name = "price_cost_total")
    private BigDecimal priceCostTotal;

    @Column(name = "price_cost_total_return")
    private BigDecimal priceCostTotalReturn;

    @Column(name = "remain_qty")
    private BigDecimal remainQty;

    @Column(name = "remian_stock_amount")
    private BigDecimal remianStockAmount;

    @Column(name = "remian_price_cost")
    private BigDecimal remianPriceCost;
}
