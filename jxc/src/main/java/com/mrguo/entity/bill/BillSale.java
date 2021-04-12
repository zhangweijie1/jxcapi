package com.mrguo.entity.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName Bill
 * @Description 单据类
 * @date 2019/9/1210:58 AM
 * @updater 郭成兴
 * @updatedate 2019/9/1210:58 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillSale extends Bill {
    
    /**
     * 关联票据Id（进出库，订单等）
     */
    @Id
    @Column(name = "bill_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    /**
     * 销售类型：0销售 1批发
     */
    private String type;

    /**
     * 是否送货
     */
    @Column(name = "is_delivery")
    private String isDelivery;


    /**
     * 送货日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    /**
     * 客户联系人
     */
    private String contacter;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 送货地址
     */
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * 是否同步到客户信息
     */
    @Transient
    private Boolean isSync;
}
