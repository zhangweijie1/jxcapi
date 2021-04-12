package com.mrguo.entity.excle;

import com.mrguo.entity.goods.GoodsBarcode;
import com.mrguo.entity.goods.GoodsPrice;
import com.mrguo.entity.goods.GoodsUnit;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName ExcelExportSkuData
 * @Description sku用于导出的实体
 * @date 2020/7/269:33 AM
 * @updater 郭成兴
 * @updatedate 2020/7/269:33 AM
 */
@Data
public class ExcelExportSkuVo {

    @Column(name = "sku_id")
    private Long skuId;

    @Column(name = "cat_name")
    private String catName;

    private String code;

    private String name;

    @Column(name = "origin_quantity")
    private BigDecimal originQuantity;

    @Column(name = "origin_price")
    private BigDecimal originPrice;

    private String specs;

    @Column(name = "is_enable_specs")
    private String isEnableSpecs;

    private List<GoodsUnit> goodsUnitList;

    private List<GoodsBarcode> barcodeList;

    private List<GoodsPrice> goodsPriceList;

    @Column(name = "min_qty_warning")
    private BigDecimal minQtyWarning;

    @Column(name = "max_qty_warning")
    private BigDecimal maxQtyWarning;

    private String remarks;

    private BigDecimal weight;

    @Column(name = "is_enable")
    private String isEnable;
}
