package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.purchase.PurchaseReturnBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */
@Api(tags = "进货退货单", description = "进货退货单，产生待出库数据")
@RestController
@RequestMapping("/bill/purchaseReturn")
public class BillPurchaseReturnController {

    @Autowired
    private PurchaseReturnBillServiceImpl billPurchaseReturnService;
    @Autowired
    private BillCommonServiceImpl billService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;


    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "purchase_return:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Validated BillAndDetailsVo<Bill> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billPurchaseReturnService.addData(billAndDetailsVo);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "purchase_return:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billPurchaseReturnService.cancle(billId);
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billPurchaseReturnService.getBillNo();
    }

    @ApiPermission(value = "purchase_return:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getSaleDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billService.getBillAndDetailsById(billId));
    }

    @ApiOperation(value = "分页查询，可以退货的单据")
    @ApiPermission(value = "purchase_return:add", pname = "新增")
    @PostMapping("/listWaite2ReturnBillsPage")
    public Result listWaite2ReturnBillsPage(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billPurchaseReturnService.listWaiteReturnPurchase(pageParams);
    }

    @ApiOperation(value = "分页查询，进货退货单")
    @ApiPermission(value = "purchase_return:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billPurchaseReturnService.listPage(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "purchase_return:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return billPurchaseReturnService.export(billId);
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "purchase_return:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billPurchaseReturnService.print(billId);
    }
}
