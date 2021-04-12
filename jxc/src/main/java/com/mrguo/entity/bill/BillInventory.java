package com.mrguo.entity.bill;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/262:22 PM
 * @updater 郭成兴
 * @updatedate 2020/6/262:22 PM
 */
@Data
public class BillInventory extends Bill {

    @Column(name = "book_qty")
    private BigDecimal bookQty;

    @Column(name = "real_qty")
    private BigDecimal realQty;
}
