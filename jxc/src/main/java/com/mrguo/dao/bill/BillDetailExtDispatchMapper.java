package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillDetailExtDispatch;
import com.mrguo.vo.bill.BillDetailDispatchVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("billDetailExtDispatchMapper")
public interface BillDetailExtDispatchMapper extends MyMapper<BillDetailExtDispatch> {

    /**
     * 调拨单明细,只有change_qty
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/6/30 8:58 PM
     * @updater 郭成兴
     * @updatedate 2020/6/30 8:58 PM
     */
    List<BillDetailExtDispatch> selectListByMasterId(@Param("billId") Long billId);

    /**
     * 调拨单(入库)明细包含billDetail
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/6/30 8:57 PM
     * @updater 郭成兴
     * @updatedate 2020/6/30 8:57 PM
     */
    List<BillDetailDispatchVo> selectListOutByMasterId(@Param("billId") Long billId);

    List<BillDetailDispatchVo> selectListInByMasterId(@Param("billId") Long billId);
}