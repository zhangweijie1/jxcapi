package com.mrguo.controller.report.business;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dto.report.SaleDetailReportDto;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.report.SaleDetailReportServiceImpl;
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
 * @Description
 * @date 2020/5/187:44 PM
 * @updater 郭成兴
 * @updatedate 2020/5/187:44 PM
 */
@Api(tags = "销售明细报表")
@RestController
@RequestMapping("/report/saleDetail")
public class SaleDetailReportController {

    @Autowired
    private SaleDetailReportServiceImpl saleDetailReportService;

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listSaleDetail")
    public Result listSaleDetail(@RequestBody PageParams<SaleDetailReportDto> pageParams) {
        return saleDetailReportService.listSaleDetail(pageParams);
    }
}
