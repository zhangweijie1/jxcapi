package com.mrguo.service.impl.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 仓库库存的 相关数量
 * @date 2020/1/610:15 PM
 * @updater 郭成兴
 * @updatedate 2020/1/610:15 PM
 */
@Data
public class StockQuantity {
    /**
     * 商品SKU的Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 仓库Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id")
    private Long storeId;

    private BigDecimal quantityIn;

    private BigDecimal quantityOut;

    private BigDecimal waitQuantityIn;

    private BigDecimal waitQuantityOut;
}
