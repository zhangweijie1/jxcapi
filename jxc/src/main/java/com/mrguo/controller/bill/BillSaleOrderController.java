package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bill.Bill;
import com.mrguo.vo.bill.BillSaleAndDetailsVo;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.sale.SaleOrderBillServiceImpl;
import com.mrguo.service.impl.bill.sale.SaleBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 郭成兴
 * @ClassName BillSaleOrderController
 * @Description 销售订单控制层
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */
@Api(tags = "销售订单",description="销售订单,对库存不产生影响的销售预定")
@RestController
@RequestMapping("/bill/saleOrder")
public class BillSaleOrderController {

    @Autowired
    private SaleOrderBillServiceImpl billSaleOrderService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;
    @Autowired
    private SaleBillServiceImpl billSaleService;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "sale_order:adedit", pname = "新增编辑")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Validated BillSaleAndDetailsVo billDto) throws Exception {
        Bill bill = billDto.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billSaleOrderService.addData(billDto);
    }

    @ApiOperation(value = "关闭单据")
    @ApiPermission(value = "sale_order:close", pname = "关闭")
    @PostMapping("/close")
    public Result close(@RequestBody Long billId) throws Exception {
        return billSaleOrderService.close(billId);
    }

    @ApiOperation(value = "编辑单据")
    @ApiPermission(value = "sale_order:adedit", pname = "新增编辑")
    @PostMapping("/updateData")
    public Result updateData(@RequestBody BillSaleAndDetailsVo billDto) throws Exception {
        return Result.ok(billSaleOrderService.updateData(billDto));
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillCode() throws Exception {
        return billSaleOrderService.getBillNo();
    }

    @ApiOperation(value = "根据单据Id，查询单据明细")
    @ApiPermission(value = "sale_order:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getDataById(@PathVariable Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "sale_order:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billSaleOrderService.listPage(pageParams);
    }

    @ApiOperation(value = "根据单据Id，查询该单据未转化的明细")
    @ApiPermission(value = "sale_order:list", pname = "查询")
    @PostMapping("/listNotTransDetailById")
    public Result listNotTransDetailById(@RequestBody Long billId) throws Exception {
        return Result.ok(billSaleService.listNotTransDetailById(billId));
    }

    @ApiOperation(value = "根据单据Id，查询该单据已转化的明细")
    @ApiPermission(value = "sale_order:list", pname = "查询")
    @PostMapping("/listHasTransDataById")
    public Result listHasTransDataById(@RequestBody Long billId) throws Exception {
        return Result.ok(billSaleService.listHasTransDataById(billId));
    }

    @ApiOperation(value = "审核单据")
    @ApiPermission(value = "sale_order:audit", pname = "审核")
    @PostMapping("/passAudit")
    public Result passAudit(@RequestBody List<Long> ids) throws Exception {
        return billSaleOrderService.passAuditList(ids);
    }

    @ApiOperation(value = "反审核单据")
    @ApiPermission(value = "sale_order:audit", pname = "审核")
    @PostMapping("/antiAudit")
    public Result antiAudit(@RequestBody List<Long> ids) throws Exception {
        return billSaleOrderService.antiAuditList(ids);
    }

    @ApiOperation(value = "导出单据")
    @ApiPermission(value = "sale_order:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return Result.ok(billSaleService.getSaleDataById(billId));
    }

    @ApiOperation(value = "打印单据")
    @ApiPermission(value = "sale_order:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billSaleOrderService.print(billId);
    }
}
