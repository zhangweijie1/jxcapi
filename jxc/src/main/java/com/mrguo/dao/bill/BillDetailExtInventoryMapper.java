package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillDetailExtInventory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName: BillDetailExtInventoryMapper
 * @Description:  扩展类Mapper
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/10 10:04 下午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Repository("billDetailExtInventoryMapper")
public interface BillDetailExtInventoryMapper extends MyMapper<BillDetailExtInventory> {

    @Select("select * from t_bill_detail_ext_inventory where bill_id = #{billId}")
    List<BillDetailExtInventory> selectListDataByMasterId(@Param("billId") Long billId);
}