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
import com.mrguo.service.impl.bill.stock.BillStockInServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 入库操作
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */

@Api(tags = "入库单",description="仓库处,入库操作")
@RestController
@RequestMapping("/bill/stockIn")
public class BillStockInController {

    @Autowired
    private BillStockInServiceImpl billStockInService;
    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "stock_in:add", pname = "入库操作")
    @PostMapping("/addData")
    public Result addData(@RequestBody BillAndDetailsVo<BillStock> billAndDetailsVo) throws Exception {
        BillStock bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        if ("dispatch".equals(bill.getRelationBillCat())) {
            return billStockInService.addStockInDispatch(billAndDetailsVo);
        } else {
            return billStockInService.addStockIn(billAndDetailsVo);
        }
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "stock_in:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billStockInService.cancleBill(billId);
    }

    @ApiOperation(value = "获取单号")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billStockInService.getBillNo();
    }

    @ApiOperation(value = "根据单据ID，获取单据明细")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getBillDetailByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billStockInService.getDefaultBillAndDetailByBillId(billId));
    }

    @ApiOperation(value = "根据单据ID，获取该单据未入库的商品")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/listWaiteInGoods/{billId}")
    public Result getWaiteGoodsByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billStockInService.getWaiteGoodsByBillId(billId));
    }

    @ApiOperation(value = "根据单据ID，获取该单据已入库商品")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/listHasInGoods/{billId}")
    public Result getHasInGoodsByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billStockInService.getHasInGoodsByBillId(billId));
    }

    @ApiOperation(value = "根据单据ID，查询待入库单据的明细")
    @PostMapping("/getBillAndDetailsOfWaite/{billId}")
    public Result getWaiteDetail(@PathVariable Long billId) throws Exception {
        Bill bill = billCommonService.getBillById(billId);
        if (BillCatEnum.dispatch.getCode().equals(bill.getBillCat())) {
            return Result.ok(billStockInService.getDispatchBillAndDetailByBillId(billId));
        } else {
            return Result.ok(billCommonService.getBillAndDetailsById(billId));
        }
    }

    @ApiOperation(value = "分页查询待入库单据")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/listWaite2InBillsPage")
    public Result listWaiteStockInBillList(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return billStockInService.listWaiteStockInBillList(pageParams);
    }

    @ApiOperation(value = "分页查询已入库单据")
    @ApiPermission(value = "stock_in:list", pname = "查询")
    @PostMapping("/listHasInBillsPage")
    public Result listHasStockInBillList(@RequestBody PageParams<BillStock> pageParams) throws Exception {
        return billStockInService.listHasStockInBillList(pageParams);
    }

    @ApiPermission(value = "stock_in:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return null;
    }

    @ApiPermission(value = "stock_in:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billStockInService.getPrintData(billId);
    }
}
