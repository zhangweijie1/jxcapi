package com.mrguo.service.inter.bill;

import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.vo.bill.BillDiapatchVo;

public interface BillDispatchService extends BillBaseService {

    Result addData(BillAndDetailsVo<BillDiapatchVo> billAndDetailsVo) throws Exception;
}
