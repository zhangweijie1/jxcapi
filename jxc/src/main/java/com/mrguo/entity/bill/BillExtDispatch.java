package com.mrguo.entity.bill;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: BillExtDispatch
 * @Description:  调拨单扩展表
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/7 9:12 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
@Table(name = "t_bill_ext_dispatch")
public class BillExtDispatch {
    /**
     * 主键
     */
    @Id
    private Long id;

    @Column(name = "store_id_out")
    private Long storeIdOut;

    @Column(name = "store_id_in")
    private Long storeIdIn;
}