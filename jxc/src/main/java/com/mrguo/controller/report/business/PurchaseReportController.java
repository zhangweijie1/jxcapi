package com.mrguo.controller.report.business;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.PurchaseReportMapper;
import com.mrguo.dto.report.PurchaseReportDto;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.dto.report.PurchaseInfoReportDto;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.report.PurchaseReportServiceImpl;
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

@Api(tags = "进货报表")
@RestController
@RequestMapping("/report/purchase")
public class PurchaseReportController {

    @Autowired
    private PurchaseReportMapper purchaseReportMapper;
    @Autowired
    private PurchaseReportServiceImpl purchaseReportService;

    @ApiOperation(value = "进货统计信息")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/getInfo")
    public Result getPurchaseInfo(@RequestBody Map<String,Object> params) {
        PurchaseInfoReportDto purchaseInfoReportDto = purchaseReportMapper.selectPurchasesInfo(params);
        Supplier supplier = purchaseReportMapper.selectMaxPurchaseAmountComego(params);
        GoodsSku goodssku = purchaseReportMapper.selectMaxPurchaseAmountGood(params);
        if (goodssku != null) {
            purchaseInfoReportDto.setGoodName(goodssku.getName());
        }
        if (supplier != null) {
            purchaseInfoReportDto.setComegoName(supplier.getName());
        }
        return Result.ok(purchaseInfoReportDto);
    }

    @ApiOperation(value = "查询进货信息，按商品分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listDataGroupByGoodPage")
    public Result listDataGroupByGoodPage(@RequestBody PageParams<PurchaseReportDto> pageParams) {
        return purchaseReportService.listDataGroupByGoodPage(pageParams);
    }

    @ApiOperation(value = "查询某商品的进货明细，按单据分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listGoodDetailGroupByBill/{skuId}")
    public Result listGoodDetailBySkuIdGroupByBillPage(@PathVariable Long skuId,
                                            @RequestBody PageParams<PurchaseReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("skuId", skuId);
        return purchaseReportService.listGoodDetailBySkuIdGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询某商品的进货明细，按供应商分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listGoodDetailGroupByComego/{skuId}")
    public Result listGoodDetailBySkuIdGroupByComegoPage(@PathVariable Long skuId,
                                              @RequestBody PageParams<PurchaseReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("skuId", skuId);
        return purchaseReportService.listGoodDetailBySkuIdGroupByComegoPage(pageParams);
    }

    @ApiOperation(value = "查询进货信息，按供货商分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listDataGroupByComegoPage")
    public Result listDataGroupByComegoPage(@RequestBody PageParams<PurchaseReportDto> pageParams) {
        return purchaseReportService.listDataGroupByComegoPage(pageParams);
    }

    @ApiOperation(value = "查询某供应商进货明细，按单据分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listComegoDetailGroupByBill/{comegoId}")
    public Result listComegoDetailByComegoIdGroupByBillPage(@PathVariable Long comegoId,
                                          @RequestBody PageParams<PurchaseReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("comegoId", comegoId);
        return purchaseReportService.listComegoDetailByComegoIdGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询某供应商进货明细，按商品分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listComegoDetailGroupByGood/{comegoId}")
    public Result listComegoDetailByComegoIdGroupByGoodPage(@PathVariable Long comegoId,
                                           @RequestBody PageParams<PurchaseReportDto> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("comegoId", comegoId);
        return purchaseReportService.listComegoDetailByComegoIdGroupByGoodPage(pageParams);
    }

    @ApiOperation(value = "查询进货信息，按单据日期分组")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listDataGroupByBillPage")
    public Result listDataGroupByBillPage(@RequestBody PageParams<PurchaseReportDto> pageParams) {
        return purchaseReportService.listDataGroupByBillPage(pageParams);
    }

    @ApiOperation(value = "查询某天的进货单")
    @ApiPermission(value = "report_purchase:list", pname = "查询")
    @PostMapping("/listBillDetail/{date}")
    public Result listBillDetailByDatePage(@PathVariable String date,
                                   @RequestBody PageParams<PurchaseReportDto> pageParams) {
        return purchaseReportService.listBillDetailByDatePage(pageParams);
    }

    @ApiPermission(value = "report_purchase:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<PurchaseReportDto> pageParams) {
        return null;
    }
}
