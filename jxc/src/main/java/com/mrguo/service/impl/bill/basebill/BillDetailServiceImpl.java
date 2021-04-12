package com.mrguo.service.impl.bill.basebill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillDetailMapper;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.service.inter.bill.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName BillDetailServiceImpl
 * @Description 单据明细
 * @date 2019/12/175:08 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:08 PM
 */
@Service("billDetailServiceImpl")
public class BillDetailServiceImpl extends BaseServiceImpl<BillDetail>
            implements BillDetailService {

    @Autowired
    private BillDetailMapper billdetailMapper;

    @Override
    public MyMapper<BillDetail> getMapper() {
        return billdetailMapper;
    }

    public void delDataByBillId(Long billId) {
        billdetailMapper.delDataByBillId(billId);
    }

    @Override
    public Integer getChangeQtyByBillId(Long billId) {
        return billdetailMapper.selectChangeQtyByBillId(billId);
    }

    @Override
    public List<BillDetail> listDataByBillId(Long billId) {
        return billdetailMapper.selectListDataByBillId(billId);
    }

    @Override
    public List<BillDetail> listMoreDataByBillId(Long billId) {
        return billdetailMapper.selectMoreListDataByBillId(billId);
    }

    @Override
    public List<BillDetail> listWaiteTransDataByBillId(Long billId) {
        return billdetailMapper.listNotTransDetailByBillId(billId);
    }

    @Override
    public List<BillDetail> listHasTransDataByBillId(Long billId) {
        return billdetailMapper.listHasTransDetailByBillId(billId);
    }
}
