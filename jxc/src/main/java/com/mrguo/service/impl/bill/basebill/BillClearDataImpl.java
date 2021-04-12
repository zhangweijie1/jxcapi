package com.mrguo.service.impl.bill.basebill;

import com.mrguo.dao.bill.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 单据的操作校验
 */
@Service
public class BillClearDataImpl {

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private BillDispatchMapper billDispatchMapper;
    @Autowired
    private BillDetailMapper billDetailMapper;
    @Autowired
    private BillExtAssembelMapper billExtAssembelMapper;
    @Autowired
    private BillExtDispatchMapper billExtDispatchMapper;
    @Autowired
    private BillExtSaleMapper billExtSaleMapper;
    @Autowired
    private BillDetailExtDispatchMapper billDetailExtDispatchMapper;
    @Autowired
    private BillDetailExtInventoryMapper billDetailExtInventoryMapper;
    @Autowired
    private BillStockMapper billStockMapper;

    /**
     * 清除单据数据
     */
    public void clearAllData() {
        // bill, billstock, bill_dispatch
    }
}
