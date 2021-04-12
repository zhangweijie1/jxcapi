package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillDetailExtInventory;
import com.mrguo.entity.bill.BillInventory;
import com.mrguo.vo.bill.BillInventoryDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("billInventoryMapper")
public interface BillInventoryMapper extends MyMapper<BillDetailExtInventory> {

    /**
     * 自定义查询
     *
     * @param page
     * @param map
     * @return
     */
    List<BillInventory> listPage(Page<BillInventory> page, @Param("record") Map<String, Object> map);

    List<BillInventoryDetailVo> selectDetailByBillId(@Param("billId") Long billId);
}