package com.mrguo.controller.report.charts;

import com.mrguo.common.entity.Result;
import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.PurchaseReportParam;
import com.mrguo.service.impl.charts.PurchaseChartsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 进货图表api
 * @date 2020/5/269:53 AM
 * @updater 郭成兴
 * @updatedate 2020/5/269:53 AM
 */

@RestController
@RequestMapping("/charts/purchase")
public class ChartsPurchaseController {

    @Autowired
    private PurchaseChartsServiceImpl purchaseChartsService;

    @PostMapping("/listby_good_groupby_day/{skuId}")
    public Result listByGoodGroupByDay(@PathVariable String skuId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setSkuId(skuId);
        List<Chart> charts = purchaseChartsService.listByGoodGroupByDay(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_good_groupby_month/{skuId}")
    public Result listByGoodGroupbyMonth(@PathVariable String skuId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setSkuId(skuId);
        List<Chart> charts = purchaseChartsService.listByGoodGroupbyMonth(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_good_groupby_year/{skuId}")
    public Result listByGoodGroupByYear(@PathVariable String skuId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setSkuId(skuId);
        List<Chart> charts = purchaseChartsService.listByGoodGroupByYear(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/get_bill_range_time_bysku/{skuId}")
    public Result getBillRangeTimeByGood(@PathVariable String skuId) throws Exception {
        Chart charts = purchaseChartsService.getBillRangeTimeByGood(skuId);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_day/{comegoId}")
    public Result listByComegoGroupByDay(@PathVariable String comegoId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setComegoId(comegoId);
        List<Chart> charts = purchaseChartsService.listByComegoGroupByDay(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_month/{comegoId}")
    public Result listByComegoGroupbyMonth(@PathVariable String comegoId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setComegoId(comegoId);
        List<Chart> charts = purchaseChartsService.listByComegoGroupbyMonth(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_year/{comegoId}")
    public Result listByComegoGroupByYear(@PathVariable String comegoId, @RequestBody PurchaseReportParam purchaseReportParam) throws Exception {
        purchaseReportParam.setComegoId(comegoId);
        List<Chart> charts = purchaseChartsService.listByComegoGroupByYear(purchaseReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/get_bill_range_time_bycomego/{comegoId}")
    public Result getBillRangeTimeByComego(@PathVariable String comegoId) throws Exception {
        Chart charts = purchaseChartsService.getBillRangeTimeByComego(comegoId);
        return Result.ok(charts);
    }
}
