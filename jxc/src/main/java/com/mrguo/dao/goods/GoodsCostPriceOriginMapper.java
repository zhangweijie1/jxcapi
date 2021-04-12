package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsCostPriceOrigin;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName GoodsCostPriceOriginMapper
 * @Description 商品期初成本价
 * 表示当期的期初成本
 * @author 郭成兴
 * @date 2020/7/30 7:34 AM
 * @updater 郭成兴
 * @updatedate 2020/7/30 7:34 AM
 */
@Repository("goodsCostPriceOriginMapper")
public interface GoodsCostPriceOriginMapper extends MyMapper<GoodsCostPriceOrigin> {

    List<GoodsCostPriceOrigin> listCostPriceOriginByStoreId(Long storeId);
}