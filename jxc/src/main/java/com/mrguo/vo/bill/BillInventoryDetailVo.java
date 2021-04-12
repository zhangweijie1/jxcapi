package com.mrguo.vo.bill;

import com.mrguo.entity.bill.BillDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName BillInventoryDetailVo
 * @Description 盘点单明细
 * @date 2020/6/261:54 PM
 * @updater 郭成兴
 * @updatedate 2020/6/261:54 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillInventoryDetailVo extends BillDetail {

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
