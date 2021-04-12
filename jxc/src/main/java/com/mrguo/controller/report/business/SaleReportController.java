package com.mrguo.controller.report.business;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.SaleReportMapper;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.dto.report.SaleReportDto;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.dto.report.SaleInfoReportDto;
import com.mrguo.dto.report.SaleReportParam;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.report.SaleReportServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/187:44 PM
 * @updater 郭成兴
 * @updatedate 2020/5/187:44 PM
 */

@Api(tags = "销售报表")
@RestController
@RequestMapping("/report/sale")
public class SaleReportController {

    @Autowired
    private SaleReportMapper saleReportMapper;
    @Autowired
    private SaleReportServiceImpl saleReportService;

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/getSaleInfo")
    public Result getSaleInfo(@RequestBody SaleReportParam saleReportParam) {
        SaleInfoReportDto saleInfoReportDto = saleReportMapper.selectSalesInfo(saleReportParam);
        Customer customer = saleReportMapper.selectMaxSaleAmountCustomer(saleReportParam);
        GoodsSku goodssku = saleReportMapper.selectMaxSaleAmountGood(saleReportParam);
        if (goodssku != null) {
            saleInfoReportDto.setGoodName(goodssku.getName());
        }
        if (customer != null) {
            saleInfoReportDto.setComegoName(customer.getName());
        }
        return Result.ok(saleInfoReportDto);
    }

    @ApiOperation(value = "查询进货信息，按单据分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listDataGroupByBillPage")
    public Result listDataGroupByBillPage(@RequestBody PageParams<SaleReportDto> pageParams) {
        return saleReportService.listDataGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询进货信息，按客户分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listDataGroupByComegoPage")
    public Result listDataGroupByComegoPage(@RequestBody PageParams<SaleReportDto> pageParams) {
        return saleReportService.listDataGroupByComegoPage(pageParams);
    }

    @ApiOperation(value = "查询进货信息，按商品分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listDataGroupByGoodPage")
    public Result listDataGroupByGoodPage(@RequestBody PageParams<SaleReportDto> pageParams) {
        return saleReportService.listDataGroupByGoodPage(pageParams);
    }

    @ApiOperation(value = "查询某天的销售单")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listBillDetail/{date}")
    public Result listBillDetailByDate(@PathVariable String date,
                                       @RequestBody PageParams<SaleReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("date", date);
        return saleReportService.listBillDetailByDate(pageParams);
    }

    @ApiOperation(value = "查询某客户销售明细，按单据分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listComegoDetailGroupByBillPage/{comegoId}")
    public Result listComegoDetailByComegoIdGroupByBillPage(@PathVariable Long comegoId,
                                                            @RequestBody PageParams<SaleReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("comegoId", comegoId);
        return saleReportService.listComegoDetailByComegoIdGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询某客户销售明细，按商品分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listComegoDetailGroupByGoodPage/{comegoId}")
    public Result listComegoDetailByComegoIdGroupByGoodPage(@PathVariable Long comegoId,
                                                            @RequestBody PageParams<SaleReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("comegoId", comegoId);
        return saleReportService.listComegoDetailByComegoIdGroupByGoodPage(pageParams);
    }

    @ApiOperation(value = "查询某商品的进货明细，按单据分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listGoodDetailGroupByBillPage/{skuId}")
    public Result listGoodDetailBySkuIdGroupByBillPage(@PathVariable Long skuId,
                                                       @RequestBody PageParams<SaleReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("skuId", skuId);
        return saleReportService.listGoodDetailBySkuIdGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询某商品的进货明细，按客户分组")
    @ApiPermission(value = "report_sale:list", pname = "查询")
    @PostMapping("/listGoodDetailGroupByComegoPage/{skuId}")
    public Result listGoodDetailBySkuIdGroupByComegoPage(@PathVariable Long skuId,
                                                         @RequestBody PageParams<SaleReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("skuId", skuId);
        return saleReportService.listGoodDetailBySkuIdGroupByComegoPage(pageParams);
    }

    @ApiPermission(value = "report_sale:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<SaleReportDto> pageParams) {
        return null;
    }
}
