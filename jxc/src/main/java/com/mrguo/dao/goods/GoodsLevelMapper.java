package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsLevel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("goodsLevelMapper")
public interface GoodsLevelMapper extends MyMapper<GoodsLevel> {

    List<GoodsLevel> selectBySkuIdsAndLevelId(@Param("skuIds") List<String> skuIds,
                                        @Param("levelId") Long levelId);
}