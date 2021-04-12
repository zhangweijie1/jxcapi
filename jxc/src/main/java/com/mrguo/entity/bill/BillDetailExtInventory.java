package com.mrguo.entity.bill;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 * @ClassName: BillDetailExtInventory
 * @Description:  
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/7 9:15 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
@Table(name = "t_bill_detail_ext_inventory")
public class BillDetailExtInventory{
    /**
     * 主键
     */
    @Id
    private Long id;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 账面数量
     */
    @Column(name = "book_qty")
    private BigDecimal bookQty;

    /**
     * 实际数量
     */
    @Column(name = "real_qty")
    private BigDecimal realQty;
}