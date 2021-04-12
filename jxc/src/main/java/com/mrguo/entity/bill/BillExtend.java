package com.mrguo.entity.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "t_bill_extend")
@Data
public class BillExtend {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 关联票据Id（进出库，订单等）
     */
    @Column(name = "bill_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    /**
     * 仓库ID
     */
    @Column(name = "store_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    /**
     * 商品数量
     */
    @Column(name = "goods_quantity")
    private Integer goodsQuantity;

    /**
     * 商品名
     */
    @Column(name = "goods_namestr")
    private String goodsNamestr;

    /**
     * 预计归还时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exp_return_time")
    private Date expReturnTime;


    /**
     * 借出类型
     */
    @Column(name = "borrow_type")
    private String borrowType;

}