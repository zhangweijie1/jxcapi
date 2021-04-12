package com.mrguo.vo.bill;

import com.mrguo.entity.bill.BillDetail;
import lombok.Data;

import javax.validation.Valid;
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
public class BillAndDetailsVo<T> {

    @Valid
    private T bill;

    private List<BillDetail> billDetailList;
}
