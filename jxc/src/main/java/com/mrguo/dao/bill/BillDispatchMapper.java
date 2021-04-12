package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillDispatch;
import com.mrguo.vo.bill.BillDiapatchVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("billDispatchMapper")
public interface BillDispatchMapper extends MyMapper<BillDispatch> {

    /**
     * 自定义查询调拨单
     *
     * @param page
     * @param data
     * @return
     */
    List<BillDiapatchVo> selectList(Page page, @Param("record") Map<String, Object> data);

    /**
     * 根据id查询调拨单bill
     *
     * @param billId
     * @return
     */
    BillDiapatchVo selectBillByBillId(@Param("billId") Long billId);

    BillDispatch selectDispatchInByMasterId(@Param("masterId") Long masterId);
}