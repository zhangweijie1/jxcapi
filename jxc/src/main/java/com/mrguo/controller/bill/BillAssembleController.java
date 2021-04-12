package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.vo.bill.BillAssembelVo;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.assembel.AssembelBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 组装拆卸
 * @date 2020/4/165:49 PM
 * @updater 郭成兴
 * @updatedate 2020/4/165:49 PM
 */

@Api(tags = "组装拆卸单",description="组装分拆商品")
@RestController
@RequestMapping("/bill/assembel")
public class BillAssembleController {

    @Autowired
    private AssembelBillServiceImpl billAssembelService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "assembel:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Valid BillAndDetailsVo<BillAssembelVo> billAndDetailsVo) throws Exception {
        Bill bill = billAndDetailsVo.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billAssembelService.addData(billAndDetailsVo);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "assembel:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody  Long billId) throws Exception {
        return billAssembelService.cancle(billId);
    }

    @ApiOperation(value = "获取单据编号")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billAssembelService.getBillNo();
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "sale:list",pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getBillAndDetailByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billAssembelService.getBillAndDetailByBillId(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "assembel:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<BillAssembelVo> pageParams) throws Exception {
        return billAssembelService.listPage(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "assembel:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long billId) throws Exception {
        return billAssembelService.export(billId);
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "assembel:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billAssembelService.print(billId);
    }
}
