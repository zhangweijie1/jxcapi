package com.mrguo.dto.report;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 利润报表支出
 * @date 2020/7/189:57 AM
 * @updater 郭成兴
 * @updatedate 2020/7/189:57 AM
 */

@Data
public class ProfitPay {

    /**
     * 日常支出
     */
    @Column(name = "daily_out")
    private String dailyOut;

    /**
     * 盘亏
     */
    @Column(name = "inventory")
    private String inventory;


    /**
     * 进货其他费用
     */
    @Column(name = "purchase_other")
    private BigDecimal purchaseOther;

    /**
     * 进货税额
     */
    private BigDecimal tax;

    /**
     * 收款优惠
     */
    @Column(name = "rece_discount")
    private String receDiscount;

    /**
     * 转账手续费
     */
    @Column(name = "trans_procedure")
    private BigDecimal transProcedure;

    /**
     * 出库运费
     */
    @Column(name = "stock_out_freight")
    private BigDecimal stockOutFreight;

    /**
     * 销售退货其他
     */
    @Column(name = "sale_return_other")
    private BigDecimal saleReturnOther;

    /**
     * 进货退货差价
     */
    @Column(name = "purchase_return_dis")
    private BigDecimal purchaseReturnDis;

    /**
     * 调拨其他费用
     */
    @Column(name = "dispatch_other")
    private BigDecimal dispatchOther;
}
