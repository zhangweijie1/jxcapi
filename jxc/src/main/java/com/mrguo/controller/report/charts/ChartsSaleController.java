package com.mrguo.controller.report.charts;

import com.mrguo.common.entity.Result;
import com.mrguo.dto.charts.Chart;
import com.mrguo.dto.report.SaleReportParam;
import com.mrguo.service.impl.charts.SaleChartsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售图表api
 * @date 2020/5/269:53 AM
 * @updater 郭成兴
 * @updatedate 2020/5/269:53 AM
 */

@RestController
@RequestMapping("/charts/sale")
public class ChartsSaleController {

    @Autowired
    private SaleChartsServiceImpl saleChartsService;

    @PostMapping("/listby_good_groupby_day/{skuId}")
    public Result listByGoodGroupByDay(@PathVariable String skuId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setSkuId(skuId);
        List<Chart> charts = saleChartsService.listByGoodGroupByDay(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listDataGroupByDay")
    public Result listDataGroupByDay(@RequestBody Map data) throws Exception {
        List<Chart> charts = saleChartsService.listDataGroupByDay(data);
        return Result.ok(charts);
    }

    @PostMapping("/listby_good_groupby_month/{skuId}")
    public Result listByGoodGroupbyMonth(@PathVariable String skuId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setSkuId(skuId);
        List<Chart> charts = saleChartsService.listByGoodGroupbyMonth(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_good_groupby_year/{skuId}")
    public Result listByGoodGroupByYear(@PathVariable String skuId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setSkuId(skuId);
        List<Chart> charts = saleChartsService.listByGoodGroupByYear(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/get_bill_range_time_bysku/{skuId}")
    public Result getBillRangeTimeByGood(@PathVariable String skuId) throws Exception {
        Chart charts = saleChartsService.getBillRangeTimeByGood(skuId);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_day/{comegoId}")
    public Result listByComegoGroupByDay(@PathVariable String comegoId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setComegoId(comegoId);
        List<Chart> charts = saleChartsService.listByComegoGroupByDay(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_month/{comegoId}")
    public Result listByComegoGroupbyMonth(@PathVariable String comegoId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setComegoId(comegoId);
        List<Chart> charts = saleChartsService.listByComegoGroupbyMonth(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/listby_comego_groupby_year/{comegoId}")
    public Result listByComegoGroupByYear(@PathVariable String comegoId, @RequestBody SaleReportParam saleReportParam) throws Exception {
        saleReportParam.setComegoId(comegoId);
        List<Chart> charts = saleChartsService.listByComegoGroupByYear(saleReportParam);
        return Result.ok(charts);
    }

    @PostMapping("/get_bill_range_time_bycomego/{comegoId}")
    public Result getBillRangeTimeByComego(@PathVariable String comegoId) throws Exception {
        Chart charts = saleChartsService.getBillRangeTimeByComego(comegoId);
        return Result.ok(charts);
    }
}
