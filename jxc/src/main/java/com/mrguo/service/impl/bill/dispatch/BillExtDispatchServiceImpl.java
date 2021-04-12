package com.mrguo.service.impl.bill.dispatch;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillExtDispatchMapper;
import com.mrguo.dao.bill.BillExtendMapper;
import com.mrguo.entity.bill.BillExtDispatch;
import com.mrguo.entity.bill.BillExtend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 周健
 * @Date: 2020/1/16 16:12
 */
@Service
public class BillExtDispatchServiceImpl extends BaseServiceImpl<BillExtDispatch> {
    @Autowired
    private BillExtDispatchMapper billExtDispatchMapper;

    @Override
    public MyMapper<BillExtDispatch> getMapper() {
        return billExtDispatchMapper;
    }
}
