package com.mrguo.entity.goods;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "bsd_goods_costprice_origin")
public class GoodsCostPriceOrigin {
    /**
     * 商品sku_id
     */
    @Id
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库ID
     */
    @Id
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 实时动态成本价（基于基础单位）
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;
}