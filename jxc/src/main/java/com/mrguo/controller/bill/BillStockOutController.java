package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillStock;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.stock.BillStockOutServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 出库操作
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */

@Api(tags = "出库单", description = "仓库处,出库操作")
@RestController
@RequestMapping("/bill/stockOut")
public class BillStockOutController {

    @Autowired
    private BillStockOutServiceImpl billStockOutService;
    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "stock_out:add", pname = "出库操作")
    @PostMapping("/addData")
    public Result addData(@RequestBody BillAndDetailsVo<BillStock> billAndDetailsVo) throws Exception {
        BillStock bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        if ("dispatch".equals(bill.getRelationBillCat())) {
            return billStockOutService.addStockOutDispatch(billAndDetailsVo);
        } else {
            return billStockOutService.addStockOut(billAndDetailsVo);
        }
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "stock_out:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billStockOutService.cancleBill(billId);
    }

    @ApiOperation(value = "获取单据号")
    @PostMapping("/getBillNo")
    public Result getBillcode() throws Exception {
        return billStockOutService.getBillNo();
    }

    @ApiOperation(value = "根据单据ID，查询单据的明细")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getBillDetailByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billStockOutService.getDefaultBillAndDetailByBillId(billId));
    }

    @ApiOperation(value = "根据单据ID，查询待出库单据的明细")
    @PostMapping("/getBillAndDetailsOfWaite/{billId}")
    public Result getWaiteDetail(@PathVariable Long billId) throws Exception {
        Bill bill = billCommonService.getBillById(billId);
        if (BillCatEnum.dispatch.getCode().equals(bill.getBillCat())) {
            return Result.ok(billStockOutService.getDispatchBillAndDetailByBillId(billId));
        } else {
            return Result.ok(billCommonService.getBillAndDetailsById(billId));
        }
    }

    @ApiOperation(value = "分页查询待出库单据")
    @ApiPermission(value = "stock_out:list", pname = "查询")
    @PostMapping("/listWaite2OutBillsPage")
    public Result listWaiteStockOutBillList(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billStockOutService.listWaiteStockOutBillList(pageParams);
    }

    @ApiOperation(value = "分页查询已出库单据")
    @ApiPermission(value = "stock_out:list", pname = "查询")
    @PostMapping("/listHasOutBillsPage")
    public Result listHasStockOutBillList(@RequestBody PageParams<BillStock> pageParams) throws Exception {
        return billStockOutService.listHasStockOutBillList(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "stock_out:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billStockOutService.listWaiteStockOutBillList(pageParams);
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "stock_out:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billStockOutService.getPrintData(billId);
    }
}
