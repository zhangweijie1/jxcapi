package com.mrguo.service.impl.bill.stock;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillStockDetailMapper;
import com.mrguo.entity.bill.BillStockDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/314:57 PM
 * @updater 郭成兴
 * @updatedate 2019/12/314:57 PM
 */
@Service
public class BillStockDetailServiceImpl extends BaseServiceImpl<BillStockDetail> {

    @Autowired
    private BillStockDetailMapper billStockDetailMapper;

    @Override
    public MyMapper<BillStockDetail> getMapper() {
        return billStockDetailMapper;
    }
}
