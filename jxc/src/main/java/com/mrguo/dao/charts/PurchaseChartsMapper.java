package com.mrguo.dao.charts;

import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.PurchaseReportParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/2610:06 AM
 * @updater 郭成兴
 * @updatedate 2020/5/2610:06 AM
 */
@Repository("purchaseChartsMapper")
public interface PurchaseChartsMapper {

    List<Chart> listByGoodGroupByDay(@Param("record") PurchaseReportParam purchaseReportParam);

    List<Chart> listByGoodGroupbyMonth(@Param("record") PurchaseReportParam purchaseReportParam);

    List<Chart> listByGoodGroupbyYear(@Param("record") PurchaseReportParam purchaseReportParam);

    /**
     * sku的销售单据时间范围
     *
     * @param skuId
     * @return
     */
    Chart getBillRangeTimeByGood(@Param("skuId") String skuId);

    List<Chart> listByComegoGroupByDay(@Param("record") PurchaseReportParam purchaseReportParam);

    List<Chart> listByComegoGroupbyMonth(@Param("record") PurchaseReportParam purchaseReportParam);

    List<Chart> listByComegoGroupbyYear(@Param("record") PurchaseReportParam purchaseReportParam);

    /**
     * 客户的销售单据时间范围
     *
     * @param comegoId
     * @return
     */
    Chart getBillRangeTimeByComego(@Param("comegoId") String comegoId);
}
