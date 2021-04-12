package com.mrguo.controller.report.fin;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dto.report.SaleReportParam;
import com.mrguo.service.impl.report.ReportFinServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 财务报表(经营状况 ， 利润报表 ， 业绩报表)
 * @date 2020/6/41:49 PM
 * @updater 郭成兴
 * @updatedate 2020/6/41:49 PM
 */
@Api(tags = "财务报表")
@RestController
@RequestMapping("/report/fin")
public class ReportFinController {

    @Autowired
    private ReportFinServiceImpl reportFinService;

    @ApiOperation(value = "获取收入项目和金额")
    @PostMapping("/getReceAmount")
    public Result getReceAmount(@RequestBody Map<String, Object> data) throws Exception {
        return reportFinService.getReceAmount(data);
    }

    @ApiOperation(value = "获取支出项目和金额")
    @PostMapping("/getPayAmount")
    public Result getPayAmount(@RequestBody Map<String, Object> data) throws Exception {
        return reportFinService.getPayAmount(data);
    }

    @ApiOperation(value = "业绩报表总览")
    @PostMapping("/getPerformanceInfo")
    public Result getStatisticsInfo(@RequestBody Map<String, Object> data) throws Exception {
        return Result.ok(reportFinService.getPerformanceInfo(data));
    }

    @ApiOperation(value = "业绩报表明细,按照员工(经手人)分组")
    @PostMapping("/listPerformanceGroupByUser")
    public Result listPerformanceGroupByUser(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return reportFinService.listPerformanceGroupByUser(pageParams);
    }

    @ApiOperation(value = "经营状况表")
    @PostMapping("/getManagerStatus")
    public Result listManagerStatus(@RequestBody SaleReportParam saleReportParam) throws Exception {
        return null;
    }
}
