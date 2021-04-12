package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillStockDetail;
import org.springframework.stereotype.Repository;

@Repository("billStockDetailMapper")
public interface BillStockDetailMapper extends MyMapper<BillStockDetail> {
}