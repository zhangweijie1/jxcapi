package com.mrguo.service.impl.bill.sale;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillExtSaleMapper;
import com.mrguo.entity.bill.BillExtSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售票据相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service("billSaleExtendServiceImpl")
public class BillSaleExtendServiceImpl extends BaseServiceImpl<BillExtSale> {

    @Autowired
    private BillExtSaleMapper billExtSaleMapper;

    @Override
    public MyMapper<BillExtSale> getMapper() {
        return billExtSaleMapper;
    }

    public BillExtSale getDataById(Long billId){
        return billExtSaleMapper.selectDataById(billId);
    }
}
