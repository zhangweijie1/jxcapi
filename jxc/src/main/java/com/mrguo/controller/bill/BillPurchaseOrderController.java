package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.purchase.PurchaseOrderBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */
@Api(tags = "进货订单", description = "进货订单，预定进货，对库存没有影响")
@RestController
@RequestMapping("/bill/purchaseOrder")
public class BillPurchaseOrderController {

    @Autowired
    private PurchaseOrderBillServiceImpl billPurchaseOrderService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "purchase_order:adedit", pname = "新增编辑")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Validated BillAndDetailsVo<Bill> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billPurchaseOrderService.addData(billAndDetailsVo);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "purchase_order:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billPurchaseOrderService.cancle(billId);
    }

    @ApiOperation(value = "编辑单据")
    @ApiPermission(value = "purchase_order:adedit", pname = "新增编辑")
    @PostMapping("/updateData")
    public Result updateData(@RequestBody BillAndDetailsVo<Bill> billAndDetailsVo) throws Exception {
        return billPurchaseOrderService.updateData(billAndDetailsVo);
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billPurchaseOrderService.getBillNo();
    }

    @ApiOperation(value = "根据单据Id，获取单据和单据明细")
    @ApiPermission(value = "purchase_order:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getSaleDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billPurchaseOrderService.getPurchaseDataById(billId));
    }

    @ApiOperation(value = "根据单据Id，查询没转化的商品（转销售单）")
    @ApiPermission(value = "purchase_order:list", pname = "查询")
    @PostMapping("/listNotTransGoods/{billId}")
    public Result listNotTransGoodsByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billPurchaseOrderService.getNotTransDetailById(billId));
    }

    @ApiOperation(value = "根据单据Id，查询已经转化的商品")
    @ApiPermission(value = "purchase_order:list", pname = "查询")
    @PostMapping("/listHasTransGoods/{billId}")
    public Result listHasTransGoodsByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billPurchaseOrderService.getHasTransDataById(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "purchase_order:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billPurchaseOrderService.listPage(pageParams);
    }

    @ApiOperation(value = "审核单据")
    @ApiPermission(value = "purchase_order:audit", pname = "审核")
    @PostMapping("/passAudit")
    public Result passAudit(@RequestBody List<Long> ids) throws Exception {
        return billPurchaseOrderService.passAuditList(ids);
    }

    @ApiOperation(value = "反审核单据")
    @ApiPermission(value = "purchase_order:audit", pname = "审核")
    @PostMapping("/antiAudit")
    public Result antiAudit(@RequestBody List<Long> ids) throws Exception {
        return billPurchaseOrderService.antiAuditList(ids);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "purchase_order:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody List<Long> ids) throws Exception {
        return billPurchaseOrderService.antiAuditList(ids);
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "purchase_order:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billPurchaseOrderService.print(billId);
    }
}
