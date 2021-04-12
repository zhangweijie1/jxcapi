package com.mrguo.dao.charts;

import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.SaleReportParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/2610:06 AM
 * @updater 郭成兴
 * @updatedate 2020/5/2610:06 AM
 */
@Repository("saleChartsMapper")
public interface SaleChartsMapper {

    List<Chart> listByGoodGroupByDay(@Param("record") SaleReportParam saleReportParam);

    List<Chart> listDataGroupByDay(@Param("record") Map data);

    List<Chart> listByGoodGroupbyMonth(@Param("record") SaleReportParam saleReportParam);

    List<Chart> listByGoodGroupbyYear(@Param("record") SaleReportParam saleReportParam);

    /**
     * sku的销售单据时间范围
     *
     * @param skuId
     * @return
     */
    Chart getBillRangeTimeByGood(@Param("skuId") String skuId);

    List<Chart> listByComegoGroupByDay(@Param("record") SaleReportParam saleReportParam);

    List<Chart> listByComegoGroupbyMonth(@Param("record") SaleReportParam saleReportParam);

    List<Chart> listByComegoGroupbyYear(@Param("record") SaleReportParam saleReportParam);

    /**
     * 客户的销售单据时间范围
     *
     * @param comegoId
     * @return
     */
    Chart getBillRangeTimeByComego(@Param("comegoId") String comegoId);
}
