package com.mrguo.dto.goods;

import com.mrguo.entity.goods.*;
import com.mrguo.entity.origin.OriginSkuCostprice;
import com.mrguo.entity.origin.OriginSkuStock;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName GoodsDto
 * @Description 商品组装实体: spu，skuList
 * @date 2019/8/1710:28 PM
 * @updater 郭成兴
 * @updatedate 2019/8/1710:28 PM
 */
@Data
public class GoodAssemblyDto {

    private GoodsSpu goodsSpu;

    /**
     * skuList
     */
    private List<GoodsSku> goodsSkuList;

    /**
     * 商品价格数据
     */
    private List<GoodsPrice> goodsPriceList;

    /**
     * 等级价
     */
    private List<GoodsLevel> goodsLevelList;

    /**
     * unit
     */
    private List<GoodsUnit> goodsUnitList;

    /**
     * 条形码
     */
    private List<GoodsBarcode> goodsBarcodeList;

    /**
     * 期初库存
     */
    private List<GoodsStock> goodsStockList;
    /**
     * 期初成本
     */
    private List<GoodsCostPrice> goodsCostPriceList;

    /**
     * 库存预警
     */
    private List<GoodsStockWarn> goodsStockWarnList;

    /**
     * 期初信息
     */
    private List<OriginSkuStock> originSkuStockList;

    /**
     * 期初成本(结存用)
     */
    private List<OriginSkuCostprice> originSkuCostpriceList;

    /**
     * 当期期初
     */
    private List<GoodsCostPriceOrigin> goodsCostPriceOriginList;
}
