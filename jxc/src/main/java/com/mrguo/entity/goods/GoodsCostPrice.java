package com.mrguo.entity.goods;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "bsd_goods_costprice")
public class GoodsCostPrice {
    /**
     * 商品sku_id
     */
    @Id
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 动态成本价（基于基础单位）
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;

    /**
     * 单据的成本总额（基于基础单位）
     */
    @Transient
    private BigDecimal billTotalPriceCost;

    /**
     * 单据的数量in baseUnit
     */
    @Transient
    private BigDecimal billTotalQtyInBaseUnit;

    /**
     * 库存量(现有存货)
     */
    @Transient
    @Column(name = "qty")
    private BigDecimal qty;
}