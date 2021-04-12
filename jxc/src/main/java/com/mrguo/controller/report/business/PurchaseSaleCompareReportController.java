package com.mrguo.controller.report.business;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dto.report.PurchaseSaleCompareReportDto;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.report.PurchaseSaleCompareReportServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 进销对比统计
 * @date 2020/5/187:44 PM
 * @updater 郭成兴
 * @updatedate 2020/5/187:44 PM
 */
@Api(tags = "进货销售对比报表")
@RestController
@RequestMapping("/report/purchaseSaleCompare")
public class PurchaseSaleCompareReportController {

    @Autowired
    private PurchaseSaleCompareReportServiceImpl purchaseSaleCompareReportService;

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "report_compare:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listCompare(@RequestBody PageParams<PurchaseSaleCompareReportDto> pageParams) {
        return purchaseSaleCompareReportService.listCompare(pageParams);
    }
}
