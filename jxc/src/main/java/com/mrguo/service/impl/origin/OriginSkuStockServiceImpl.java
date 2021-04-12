package com.mrguo.service.impl.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.origin.OriginSkuStockMapper;
import com.mrguo.entity.origin.OriginSkuStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/158:38 AM
 * @updater 郭成兴
 * @updatedate 2020/7/158:38 AM
 */

@Service
public class OriginSkuStockServiceImpl extends BaseServiceImpl<OriginSkuStock> {

    @Autowired
    private OriginSkuStockMapper originSkuStockMapper;

    @Override
    public MyMapper<OriginSkuStock> getMapper() {
        return originSkuStockMapper;
    }
}
