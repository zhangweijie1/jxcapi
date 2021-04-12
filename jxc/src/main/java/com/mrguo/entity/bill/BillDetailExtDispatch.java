package com.mrguo.entity.bill;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 * @ClassName: BillDetailExtDispatch
 * @Description:  调拨单明细扩展表
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/7 9:12 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
@Table(name = "t_bill_detail_ext_dispatch")
public class BillDetailExtDispatch {
    /**
     * 明细的主键
     */
    @Id
    private Long id;

    /**
     * 单据ID
     */
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 调出变动数量
     */
    @Column(name = "change_quantity_out")
    private BigDecimal changeQuantityOut;

    /**
     * 调出变动数量
     */
    @Column(name = "change_quantity_in")
    private BigDecimal changeQuantityIn;
}