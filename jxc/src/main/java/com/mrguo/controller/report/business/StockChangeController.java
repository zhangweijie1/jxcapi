package com.mrguo.controller.report.business;

import com.mrguo.common.entity.Result;
import com.mrguo.dto.report.SaleReportParam;
import com.mrguo.interfaces.ApiPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 全能库存变动表
 * @date 2020/6/41:21 PM
 * @updater 郭成兴
 * @updatedate 2020/6/41:21 PM
 */
@Api(tags = "全能库存变动报表")
@RestController
@RequestMapping("/report/stockChange")
public class StockChangeController {

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "report_stock_change:list", pname = "查询")
    @PostMapping("/list")
    public Result list(@RequestBody SaleReportParam saleReportParam) {
        return null;
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "report_stock_change:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody SaleReportParam saleReportParam) {
        return null;
    }

}
