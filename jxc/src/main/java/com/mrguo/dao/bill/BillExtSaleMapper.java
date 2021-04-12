package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillExtSale;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("billExtSaleMapper")
public interface BillExtSaleMapper extends MyMapper<BillExtSale> {

    BillExtSale selectDataById(@Param("billId") Long billId);
}