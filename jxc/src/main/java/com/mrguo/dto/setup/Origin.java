package com.mrguo.dto.setup;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/1511:21 AM
 * @updater 郭成兴
 * @updatedate 2020/6/1511:21 AM
 */
@Data
public class Origin {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String code;

    private String name;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "origin_quantity")
    private BigDecimal originQuantity;

    @Column(name = "origin_cost_price_total")
    private BigDecimal originCostPriceTotal;
}
