package com.mrguo.vo.bill;

import com.mrguo.entity.bill.BillDetail;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class BillDetailDispatchVo extends BillDetail {

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