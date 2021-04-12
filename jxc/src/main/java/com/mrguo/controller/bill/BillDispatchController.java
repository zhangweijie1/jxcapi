package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.vo.bill.BillDiapatchVo;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDispatchCancleBillServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDispatchServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 调拨单
 * @date 2020/4/165:53 PM
 * @updater 郭成兴
 * @updatedate 2020/4/165:53 PM
 */

@Api(tags = "调拨单", description = "仓库之间商品调拨")
@RestController
@RequestMapping("/bill/dispatch")
public class BillDispatchController {

    @Autowired
    private BillDispatchServiceImpl billDispatchService;
    @Autowired
    private BillDispatchCancleBillServiceImpl billDispatchCancleBillService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "dispatch:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody BillAndDetailsVo<BillDiapatchVo> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billDispatchService.addData(billAndDetailsVo);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "dispatch:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billDispatchCancleBillService.cancleBill(billId);
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billDispatchService.getBillNo();
    }

    @ApiOperation(value = "分页查询单据")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getBillAndDetailByBillId(@RequestBody Long billId) throws Exception {
        return Result.ok(billDispatchService.getBillAndDetailByBillId(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "dispatch:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<BillDiapatchVo> pageParams) throws Exception {
        return billDispatchService.listPage(pageParams);
    }

    @ApiPermission(value = "dispatch:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return billDispatchService.export(billId);
    }

    @ApiPermission(value = "dispatch:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billDispatchService.print(billId);
    }
}
