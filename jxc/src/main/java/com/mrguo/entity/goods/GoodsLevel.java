package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "bsd_goods_level")
public class GoodsLevel {
    /**
     * sku_id
     */
    @Id
    @Column(name = "sku_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    /**
     * 等级Id
     */
    @Id
    @Column(name = "level_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long levelId;

    /**
     * 折扣
     */
    private BigDecimal discount;
}