package com.mrguo.service.impl.basedata.goods.utils;

import com.mrguo.entity.excle.ExcelExportSkuVo;
import com.mrguo.entity.goods.GoodsBarcode;
import com.mrguo.entity.goods.GoodsPrice;
import com.mrguo.entity.goods.GoodsUnit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: GoodsExcleUtils
 * @Description: 商品Excle相关数据
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 11:17 上午
 * @Copyright 南通市韶光科技有限公司
 **/
public class GoodsExcleUtils {

    /**
     * 补全并导出数据 Excle
     *
     * @param excelExportSkuVoList sku excle data
     * @param goodsUnitList        单位
     * @param goodsBarcodeList     条形码
     * @param goodsPriceList       价格
     * @return
     */
    public static void completeExcelExportGoodsData(List<ExcelExportSkuVo> excelExportSkuVoList,
                                                                      List<GoodsUnit> goodsUnitList,
                                                                      List<GoodsBarcode> goodsBarcodeList,
                                                                      List<GoodsPrice> goodsPriceList) {

        for (ExcelExportSkuVo exportGoodsData : excelExportSkuVoList) {
            List<GoodsUnit> goodsUnitFilter = goodsUnitList.stream().filter(item -> {
                return item.getSkuId().equals(exportGoodsData.getSkuId());
            }).collect(Collectors.toList());
            List<GoodsBarcode> goodsBarcodeFilter = goodsBarcodeList.stream().filter(item -> {
                return item.getSkuId().equals(exportGoodsData.getSkuId());
            }).collect(Collectors.toList());
            List<GoodsPrice> goodsPriceFilter = goodsPriceList.stream().filter(item -> {
                return item.getSkuId().equals(exportGoodsData.getSkuId());
            }).collect(Collectors.toList());
            //
            exportGoodsData.setGoodsUnitList(goodsUnitFilter);
            exportGoodsData.setBarcodeList(goodsBarcodeFilter);
            exportGoodsData.setGoodsPriceList(goodsPriceFilter);
        }
    }
}
