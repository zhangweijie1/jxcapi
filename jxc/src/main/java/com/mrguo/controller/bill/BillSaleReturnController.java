package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.sale.SaleReturnBillServiceImpl;
import com.mrguo.service.impl.bill.sale.SaleBillServiceImpl;
import com.mrguo.validation.bill.SaleReturnAdd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author 郭成兴
 * @ClassName BillSaleReturnController
 * @Description 销售退货控制层
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */
@Api(tags = "销售退货单",description="销售退货单,销售产生待入库数据")
@RestController
@RequestMapping("/bill/saleReturn")
public class BillSaleReturnController {

    @Autowired
    private SaleReturnBillServiceImpl billSaleReturnService;
    @Autowired
    private SaleBillServiceImpl billSaleService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "sale_return:add",pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Validated(SaleReturnAdd.class) BillAndDetailsVo<Bill> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billSaleReturnService.addData(billAndDetailsVo);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "sale_return:cancle",pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billSaleReturnService.cancle(billId);
    }

    @ApiOperation(value = "获取单号")
    @PostMapping("/getBillNo")
    public Result getBillCode() throws Exception {
        return billSaleReturnService.getBillNo();
    }

    @ApiOperation(value = "根据单据Id，查询单据和单据明细")
    @ApiPermission(value = "sale_return:list",pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "sale_return:list",pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billSaleReturnService.listPage(pageParams);
    }

    @ApiOperation(value = "分页查询可以退货的单据", notes = "转化数量和退货数量，不超过原有数量的单据")
    @ApiPermission(value = "sale_return:add",pname = "新增")
    @PostMapping("/listWaite2ReturnBillsPage")
    public Result listWaiteReturnSale(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billSaleReturnService.listWaiteReturnBillsPage(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "sale_return:export",pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "sale_return:print",pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billSaleReturnService.print(billId);
    }
}
