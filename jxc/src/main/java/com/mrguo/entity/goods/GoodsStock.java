package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName GoodsStock
 * @Description 库存实体
 * @date 2019/11/13 3:07 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 3:07 PM
 */
@Data
@Table(name = "bsd_goods_stock")
public class GoodsStock {
    /**
     * 商品SKU的Id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库Id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id")
    private Long storeId;

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
}