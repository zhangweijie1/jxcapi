package com.mrguo.service.impl.charts;

import com.mrguo.dao.charts.PurchaseChartsMapper;
import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.PurchaseReportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/2610:00 AM
 * @updater 郭成兴
 * @updatedate 2020/5/2610:00 AM
 */

@Service
public class PurchaseChartsServiceImpl {

    @Autowired
    private PurchaseChartsMapper purchaseChartsMapper;

    public List<Chart> listByGoodGroupByDay(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByGoodGroupByDay(purchaseReportParam);
    }

    public List<Chart> listByGoodGroupbyMonth(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByGoodGroupbyMonth(purchaseReportParam);
    }

    public List<Chart> listByGoodGroupByYear(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByGoodGroupbyYear(purchaseReportParam);
    }

    public Chart getBillRangeTimeByGood(String skuId) {
        return purchaseChartsMapper.getBillRangeTimeByGood(skuId);
    }

    public List<Chart> listByComegoGroupByDay(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByComegoGroupByDay(purchaseReportParam);
    }

    public List<Chart> listByComegoGroupbyMonth(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByComegoGroupbyMonth(purchaseReportParam);
    }

    public List<Chart> listByComegoGroupByYear(PurchaseReportParam purchaseReportParam) {
        return purchaseChartsMapper.listByComegoGroupbyYear(purchaseReportParam);
    }

    public Chart getBillRangeTimeByComego(String comegoId) {
        return purchaseChartsMapper.getBillRangeTimeByComego(comegoId);
    }
}
