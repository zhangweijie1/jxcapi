package com.mrguo.entity.goods;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "bsd_goods_barcode")
public class GoodsBarcode {
    /**
     * 商品SKU的Id
     */
    @Id
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 单位Id
     */
    @Id
    @Column(name = "unit_id")
    private Long unitId;

    /**
     * 条形码
     */
    private String barcode;
}