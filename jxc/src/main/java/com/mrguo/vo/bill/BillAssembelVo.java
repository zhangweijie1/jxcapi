package com.mrguo.vo.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mrguo.entity.bill.Bill;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;


/**
 * @author 郭成兴
 * @ClassName
 * @Description 调拨单
 * @date 2019/12/175:29 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:29 PM
 */
@Data
public class BillAssembelVo extends Bill {

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id_out")
    private Long storeIdOut;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id_in")
    private Long storeIdIn;

    @Column(name = "store_name_out")
    private String storeNameOut;

    @Column(name = "store_name_in")
    private String storeNameIn;

    @Column(name = "goods_name_out")
    private String goodsNameOut;

    @Column(name = "goods_name_in")
    private String goodsNameIn;

    @Transient
    @Column(name = "change_quantity_in")
    private BigDecimal changeQuantityIn;

    @Transient
    @Column(name = "change_quantity_out")
    private BigDecimal changeQuantityOut;

    @Transient
    @Column(name = "total_price_in")
    private BigDecimal totalPriceIn;

    @Transient
    @Column(name = "total_price_out")
    private BigDecimal totalPriceOut;
}
