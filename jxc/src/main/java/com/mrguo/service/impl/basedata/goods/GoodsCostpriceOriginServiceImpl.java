package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsCostPriceOriginMapper;
import com.mrguo.entity.goods.GoodsCostPriceOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 商品期初成本价格
 * @date 2020/1/82:22 PM
 * @updater 郭成兴
 * @updatedate 2020/1/82:22 PM
 */
@Service("goodsCostpriceOriginServiceImpl")
public class GoodsCostpriceOriginServiceImpl extends BaseServiceImpl<GoodsCostPriceOrigin> {

    @Autowired
    private GoodsCostPriceOriginMapper goodsCostPriceOriginMapper;

    @Override
    public MyMapper<GoodsCostPriceOrigin> getMapper() {
        return goodsCostPriceOriginMapper;
    }

    public List<GoodsCostPriceOrigin> listCostPriceOriginByStoreId(Long storeId) {
        return goodsCostPriceOriginMapper.listCostPriceOriginByStoreId(storeId);
    }

}
