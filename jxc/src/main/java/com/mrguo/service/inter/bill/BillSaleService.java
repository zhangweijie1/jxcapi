package com.mrguo.service.inter.bill;

import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillSaleAndDetailsVo;

public interface BillSaleService extends BillBaseService {

    Result addData(BillSaleAndDetailsVo saleDto) throws Exception;
}
