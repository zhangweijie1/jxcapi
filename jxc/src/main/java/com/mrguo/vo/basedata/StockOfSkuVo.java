package com.mrguo.vo.basedata;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @ClassName: StockOfSkuVo
 * @Description: sku的库存info
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 9:30 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
public class StockOfSkuVo {

    private String id;

    private String code;

    private String name;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "origin_quantity")
    private BigDecimal originQuantity;

    @Column(name = "wait_quantity_in")
    private BigDecimal waitQuantityIn;

    @Column(name = "wait_quantity_out")
    private BigDecimal waitQuantityOut;

    @Column(name = "quantity_in")
    private BigDecimal quantityIn;

    @Column(name = "quantity_out")
    private BigDecimal quantityOut;

    @Column(name = "unit_id_str")
    private String unitIdStr;

    @Column(name = "unit_name_str")
    private String unitNameStr;

    @Column(name = "unit_isbase_str")
    private String unitIsbaseStr;

    @Column(name = "unit_multi_str")
    private String unitMultiStr;
}
