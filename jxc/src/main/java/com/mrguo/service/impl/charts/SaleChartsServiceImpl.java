package com.mrguo.service.impl.charts;

import com.mrguo.dao.charts.SaleChartsMapper;
import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.SaleReportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/2610:00 AM
 * @updater 郭成兴
 * @updatedate 2020/5/2610:00 AM
 */

@Service
public class SaleChartsServiceImpl {

    @Autowired
    private SaleChartsMapper saleChartsMapper;

    public List<Chart> listByGoodGroupByDay(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByGoodGroupByDay(saleReportParam);
    }

    public List<Chart> listDataGroupByDay(Map data) {
        return saleChartsMapper.listDataGroupByDay(data);
    }

    public List<Chart> listByGoodGroupbyMonth(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByGoodGroupbyMonth(saleReportParam);
    }

    public List<Chart> listByGoodGroupByYear(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByGoodGroupbyYear(saleReportParam);
    }

    public Chart getBillRangeTimeByGood(String skuId) {
        return saleChartsMapper.getBillRangeTimeByGood(skuId);
    }

    public List<Chart> listByComegoGroupByDay(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByComegoGroupByDay(saleReportParam);
    }

    public List<Chart> listByComegoGroupbyMonth(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByComegoGroupbyMonth(saleReportParam);
    }

    public List<Chart> listByComegoGroupByYear(SaleReportParam saleReportParam) {
        return saleChartsMapper.listByComegoGroupbyYear(saleReportParam);
    }

    public Chart getBillRangeTimeByComego(String comegoId) {
        return saleChartsMapper.getBillRangeTimeByComego(comegoId);
    }
}
