package com.mrguo.vo.bill;

import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillExtSale;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/175:29 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:29 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillSaleAndDetailsVo extends BillAndDetailsVo<Bill> {

    @Valid
    private BillExtSale billExtSale;
}
