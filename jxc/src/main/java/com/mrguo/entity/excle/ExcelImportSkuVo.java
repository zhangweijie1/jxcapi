package com.mrguo.entity.excle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/269:33 AM
 * @updater 郭成兴
 * @updatedate 2020/7/269:33 AM
 */
@Data
public class ExcelImportSkuVo {

    private String cat;

    private String code;

    private String name;

    private BigDecimal originQty;

    private BigDecimal originPrice;

    private BigDecimal originTotalPrice;

    private List<String> specs;

    private List<String> units;

    private List<String> barcodes;

    private List<BigDecimal> priceSale;

    /**
     * 批发价
     */
    private List<BigDecimal> priceWholeSale;

    private List<BigDecimal> priceMinSale;

    /**
     * 参考进货价
     */
    private List<BigDecimal> pricePurchase;

    private List<BigDecimal> stockWarn;

    private BigDecimal weight;

    private String remarks;

    private String isEnable;
}
