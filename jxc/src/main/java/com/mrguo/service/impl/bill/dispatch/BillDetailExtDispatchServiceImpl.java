package com.mrguo.service.impl.bill.dispatch;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillDetailExtDispatchMapper;
import com.mrguo.entity.bill.BillDetailExtDispatch;
import com.mrguo.vo.bill.BillDetailDispatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/2610:41 AM
 * @updater 郭成兴
 * @updatedate 2020/6/2610:41 AM
 */

@Service
public class BillDetailExtDispatchServiceImpl extends BaseServiceImpl<BillDetailExtDispatch> {

    @Autowired
    private BillDetailExtDispatchMapper billDetailExtDispatchMapper;

    @Override
    public MyMapper<BillDetailExtDispatch> getMapper() {
        return billDetailExtDispatchMapper;
    }

    public List<BillDetailExtDispatch> getListDataByBillId(Long billId) {
        return billDetailExtDispatchMapper.selectListByMasterId(billId);
    }

    public List<BillDetailDispatchVo> getListInDataByBillId(Long billId) {
        return billDetailExtDispatchMapper.selectListInByMasterId(billId);
    }

    public List<BillDetailDispatchVo> getListOutDataByBillId(Long billId) {
        return billDetailExtDispatchMapper.selectListOutByMasterId(billId);
    }
}
