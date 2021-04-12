package com.mrguo.entity.goods;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 * @author 郭成兴
 * @ClassName GoodsStockWarn
 * @Description 库存预警
 * @date 2020/7/29 5:00 PM
 * @updater 郭成兴
 * @updatedate 2020/7/29 5:00 PM
 */
@Data
@Table(name = "bsd_goods_stock_warn")
public class GoodsStockWarn {
    /**
     * 商品SKU的Id
     */
    @Id
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库Id
     */
    @Id
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 最小库存预警
     */
    @Column(name = "min_qty_warning")
    private BigDecimal minQtyWarning;

    /**
     * 最大库存预警
     */
    @Column(name = "max_qty_warning")
    private BigDecimal maxQtyWarning;
}