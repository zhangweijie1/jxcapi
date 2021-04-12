package com.mrguo.service.impl.bill.inventory;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillDetailExtInventoryMapper;
import com.mrguo.entity.bill.BillDetailExtInventory;
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
public class BillDetailExtInventoryServiceImpl extends BaseServiceImpl<BillDetailExtInventory> {

    @Autowired
    private BillDetailExtInventoryMapper billDetailExtInventoryMapper;

    @Override
    public MyMapper<BillDetailExtInventory> getMapper() {
        return billDetailExtInventoryMapper;
    }

    public List<BillDetailExtInventory> getListDataByMasterId(Long billId) {
        return billDetailExtInventoryMapper.selectListDataByMasterId(billId);
    }
}
