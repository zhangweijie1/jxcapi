package com.mrguo.service.impl.bill.assembel;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillExtAssembelMapper;
import com.mrguo.entity.bill.BillExtAssembel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/39:22 AM
 * @updater 郭成兴
 * @updatedate 2020/7/39:22 AM
 */
@Service
public class BillExtAssembelServiceImpl extends BaseServiceImpl<BillExtAssembel> {

    @Autowired
    private BillExtAssembelMapper billExtAssembelMapper;

    @Override
    public MyMapper<BillExtAssembel> getMapper() {
        return billExtAssembelMapper;
    }
}
