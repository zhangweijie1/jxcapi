package com.mrguo.entity.bill;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName BillDetailStock
 * @Description 包含商品实际库存信息的详情
 * @date 2019/9/1210:58 AM
 * @updater 郭成兴
 * @updatedate 2019/9/1210:58 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BillDetailStock extends BillDetail {
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
}
