package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "bsd_goods_unit")
public class GoodsUnit {
    /**
     * 单位Id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "unit_id")
    private Long unitId;

    /**
     * sku_id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 相对基础单位的系数,1就是基础单位
     */
    private BigDecimal multi;

    /**
     * 是否基础单位
     */
    @Column(name = "is_base")
    private String isBase;

    /**
     * 单位名
     */
    @Transient
    @Column(name = "unit_name")
    private String unitName;
}