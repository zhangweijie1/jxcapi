package com.mrguo.vo.goods;

import com.mrguo.dto.goods.SkuInfoDto;
import com.mrguo.entity.goods.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName GoodsDto
 * @Description 商品新增VO
 * @date 2019/8/1710:28 PM
 * @updater 郭成兴
 * @updatedate 2019/8/1710:28 PM
 */
@Data
public class GoodSkuAllInfoVo {

    /**
     * sku
     */
    @NotNull(message = "SKU信息没有为空")
    private GoodsSku goodsSku;

    /**
     * 商品价格数据
     */
    @NotNull(message = "商品价格不能为空")
    private List<GoodsPrice> goodsPriceList;

    /**
     * 商品等级价
     */
    private List<GoodsLevel> goodsLevelList;

    /**
     * 商品单位
     */
    private List<GoodsUnit> goodsUnitList;

    /**
     * 商品条形码
     */
    private List<GoodsBarcode> goodsBarcodeList;

    /**
     * 期初库存
     */
    @NotNull(message = "期初库存不能为空")
    private List<SkuInfoDto> goodsStockList;

    /**
     * 库存预警
     */
    private List<GoodsStockWarn> goodsStockWarnList;
}
