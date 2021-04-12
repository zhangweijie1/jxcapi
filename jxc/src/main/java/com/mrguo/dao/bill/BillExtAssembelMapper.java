package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillExtAssembel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("billExtAssembelMapper")
public interface BillExtAssembelMapper extends MyMapper<BillExtAssembel> {

}