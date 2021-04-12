package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillExtDispatch;
import com.mrguo.entity.bill.BillExtend;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillExtDispatchMapper extends MyMapper<BillExtDispatch> {


}