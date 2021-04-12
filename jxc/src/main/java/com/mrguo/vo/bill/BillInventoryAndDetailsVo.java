package com.mrguo.vo.bill;

import com.mrguo.entity.bill.Bill;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/175:29 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:29 PM
 */
@Data
public class BillInventoryAndDetailsVo {

    private Bill bill;

    private List<BillInventoryDetailVo> billDetailExtInventoryList;
}
