package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillSaleAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillSale;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.sale.SaleBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售单
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */

@Api(tags = "销售单",description="销售单,销售产生待出库数据")
@RestController
@RequestMapping("/bill/sale")
public class BillSaleController {

    @Autowired
    private SaleBillServiceImpl billSaleService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "sale:add", pname = "新增")
    @PostMapping("/addData")
    public Result addSale(@RequestBody @Validated BillSaleAndDetailsVo billDto) throws Exception {
        Bill bill = billDto.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billSaleService.addData(billDto);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "sale:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancleSale(@RequestBody Long billId) throws Exception {
        return billSaleService.cancle(billId);
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillCode() throws Exception {
        return billSaleService.getBillNo();
    }

    @ApiOperation(value = "根据单据Id，查询单据明细")
    @ApiPermission(value = "sale:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getSaleDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "sale:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listSale(@RequestBody PageParams<BillSale> pageParams) throws Exception {
        return billSaleService.listPage(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "sale:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "sale:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billSaleService.getPrintData(billId);
    }
}
