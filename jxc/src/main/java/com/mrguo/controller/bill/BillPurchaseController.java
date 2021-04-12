package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.purchase.PurchaseBillServiceImpl;
import io.swagger.annotations.Api;
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
@Api(tags = "进货单", description = "进货单，产生待入库数据")
@RestController
@RequestMapping("/bill/purchase")
public class BillPurchaseController {

    @Autowired
    private PurchaseBillServiceImpl billPurchaseService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiPermission(value = "purchase:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Validated BillAndDetailsVo<Bill> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billPurchaseService.addData(billAndDetailsVo);
    }

    @PostMapping("/getBillNo")
    public Result getBillCode() throws Exception {
        return billPurchaseService.getBillNo();
    }

    @ApiPermission(value = "purchase:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billPurchaseService.cancle(billId);
    }

    @PostMapping("/listPage")
    public Result listPurchase(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billPurchaseService.listPage(pageParams);
    }

    @ApiPermission(value = "purchase:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getSaleDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billPurchaseService.getBillAndDetailById(billId));
    }

    @ApiPermission(value = "purchase:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return billPurchaseService.export(billId);
    }

    @ApiPermission(value = "purchase:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billPurchaseService.getPrintData(billId);
    }
}
