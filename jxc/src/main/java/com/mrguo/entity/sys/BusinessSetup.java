package com.mrguo.entity.sys;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName BusinessSetup
 * @Description 业务设置的数据
 * @date 2020/6/162:25 PM
 * @updater 郭成兴
 * @updatedate 2020/6/162:25 PM
 */
@Data
public class BusinessSetup {

    /**
     * 是否允许负库存（销售，调拨）
     */
    private String isAllowNegStock;

    /**
     * 是否检查销售最低价（销售时）
     */
    private String isValiMinSalePrice;

    /**
     * 是否开启序列号
     */
    private String isOpenSerial;

    /**
     * 其否启用进货税率
     */
    private String isOpenPurchaseTax;

    /**
     * 进货税率(百分比)
     */
    private BigDecimal purchaseTax;

    /**
     * 其否启用销售税率
     */
    private String isOpenSaleTax;

    /**
     * 销售税率(百分比)
     */
    private BigDecimal saleTax;

    /**
     * 是否开启等级价
     */
    private String isOpenLevel;
}
