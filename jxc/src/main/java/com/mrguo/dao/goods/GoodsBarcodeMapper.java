package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsBarcode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: GoodsBarcodeMapper
 * @Description:
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/10 10:05 下午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Repository("goodsBarcodeMapper")
public interface GoodsBarcodeMapper extends MyMapper<GoodsBarcode> {

    @Delete("delete from bsd_goods_barcode where sku_id = #{skuId}")
    int delDataBySkuId(@Param("skuId") Long skuId);

    int delDataBySkuIds(List<String> skuIds);

    /**
     * 根据barcode查询code， 用于判断code是否在数据库存在
     * 并得到哪几个存在
     *
     * @param barcodes
     * @return
     */
    List<String> selectBarcodeListByCodes(@Param("list") List<String> barcodes);

    /**
     * 查询skuid的条形码
     *
     * @param skuId
     * @return
     */
    List<String> selectBarcodeListBySkuId(@Param("skuId") Long skuId);
}