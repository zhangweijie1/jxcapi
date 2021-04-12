package com.mrguo.entity.bill;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName: BillExtAssembel
 * @Description:  组装拆卸扩展表
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/7 9:11 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
@Table(name = "t_bill_ext_assembel")
public class BillExtAssembel {
    /**
     * billId
     */
    @Id
    private Long id;


    /**
     * 出库仓库
     */
    @Column(name = "store_id_out")
    private Long storeIdOut;


    /**
     * 入库仓库
     */
    @Column(name = "store_id_in")
    private Long storeIdIn;

    @Column(name = "goods_name_out")
    private String goodsNameOut;

    @Column(name = "goods_name_in")
    private String goodsNameIn;
}