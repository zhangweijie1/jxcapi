package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsUnit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("goodsUnitMapper")
public interface GoodsUnitMapper extends MyMapper<GoodsUnit> {

    @Delete("delete from bsd_goods_unit where sku_id = #{skuId}")
    int delDataBySkuId(@Param("skuId") Long skuId);

    int delDataBySkuIds(List<String> skuIds);

    @Delete("delete from bsd_goods_unit where sku_id = #{skuId} and unit_id = #{unitId}")
    int delDataBySkuIdUnitId(@Param("skuId") Long skuId,
                             @Param("unitId") Long unitId);

    @Select("select tgu.*, tu.name as unit_name from bsd_goods_unit tgu\n" +
            "left join bsd_unit tu on tu.id = tgu.unit_id\n" +
            "where sku_id = #{skuId}")
    List<GoodsUnit> getListBySkuId(Long skuId);

    @Select("select count(1) from bsd_goods_unit where unit_id = #{unitId}")
    int countByUnitId(Long unitId);

    List<GoodsUnit> listAllData();
}