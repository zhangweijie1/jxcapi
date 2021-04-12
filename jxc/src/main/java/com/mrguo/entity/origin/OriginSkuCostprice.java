package com.mrguo.entity.origin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * @ClassName: OriginSkuCostprice
 * @Description: 期初成本表
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/12/10 3:30 下午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Data
@Table(name = "origin_sku_costprice")
public class OriginSkuCostprice {
    /**
     * 主键（SKU_ID）
     */
    @Id
    private Long id;

    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库Id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 期初成本额
     */
    private BigDecimal costprice;

    @Column(name = "balance_time")
    private Date balanceTime;

    @Column(name = "parent_id")
    private Long parentId;
}