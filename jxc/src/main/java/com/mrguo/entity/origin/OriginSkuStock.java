package com.mrguo.entity.origin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "origin_sku_stock")
public class OriginSkuStock {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * SKU_ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库ID
     */
    @Column(name = "store_id")
    private Long storeId;

    private BigDecimal qty;

    /**
     * 期初库存
     */
    @Column(name = "origin_quantity")
    private BigDecimal originQuantity;

    /**
     * 入库量
     */
    @Column(name = "quantity_in")
    private BigDecimal quantityIn;

    /**
     * 出库量
     */
    @Column(name = "quantity_out")
    private BigDecimal quantityOut;

    /**
     * 待入库量
     */
    @Column(name = "wait_quantity_in")
    private BigDecimal waitQuantityIn;

    /**
     * 待出库量
     */
    @Column(name = "wait_quantity_out")
    private BigDecimal waitQuantityOut;


    @Column(name = "balance_time")
    private Date balanceTime;

    @Column(name = "parent_id")
    private Long parentId;
}