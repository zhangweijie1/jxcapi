package com.mrguo.service.impl.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.origin.OriginSkuCostpriceMapper;
import com.mrguo.entity.origin.OriginSkuCostprice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/158:39 AM
 * @updater 郭成兴
 * @updatedate 2020/7/158:39 AM
 */

@Service
public class OriginSkuCostpriceServiceImpl extends BaseServiceImpl<OriginSkuCostprice> {

    @Autowired
    private OriginSkuCostpriceMapper originSkuCostpriceMapper;

    @Override
    public MyMapper<OriginSkuCostprice> getMapper() {
        return originSkuCostpriceMapper;
    }

}
