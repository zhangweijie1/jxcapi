package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsPrice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("goodspriceMapper")
public interface GoodspriceMapper extends MyMapper<GoodsPrice> {

    @Delete("delete from bsd_goods_price where sku_id = #{skuId}")
    int delDataBySkuId(@Param("skuId")Long skuId);

    int delDataBySkuIds(List<String> skuIds);

    @Select("select * from bsd_goods_price where sku_id = #{skuId} and unit_id = #{unitId}")
    GoodsPrice selectOneBySkuAndUnitId(@Param("skuId")Long skuId, @Param("unitId")Long unitId);
}