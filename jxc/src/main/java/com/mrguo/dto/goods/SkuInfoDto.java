package com.mrguo.dto.goods;

import com.mrguo.entity.goods.GoodsStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @ClassName: SkuInfoDto
 * @Description: sku的信息
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 9:47 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SkuInfoDto extends GoodsStock {

    /**
     * 成本价
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 仓库名称
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 期初成本价
     */
    @Column(name = "origin_cost_price")
    private BigDecimal originCostPrice;

    /**
     * 最小预警
     */
    @Column(name = "min_qty_warning")
    private BigDecimal minQtyWarning;

    /**
     * 最大预警
     */
    @Column(name = "max_qty_warning")
    private BigDecimal maxQtyWarning;
}
