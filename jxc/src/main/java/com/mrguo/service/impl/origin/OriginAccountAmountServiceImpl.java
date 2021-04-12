package com.mrguo.service.impl.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.origin.OriginAccountAmountMapper;
import com.mrguo.entity.origin.OriginAccountAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/158:43 AM
 * @updater 郭成兴
 * @updatedate 2020/7/158:43 AM
 */

@Service
public class OriginAccountAmountServiceImpl extends BaseServiceImpl<OriginAccountAmount> {

    @Autowired
    private OriginAccountAmountMapper originAccountAmountMapper;

    @Override
    public MyMapper<OriginAccountAmount> getMapper() {
        return originAccountAmountMapper;
    }
}
