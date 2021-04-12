package com.mrguo.controller.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.bill.BillInventoryAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillInventory;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.inventory.BillInventoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author 郭成兴
 * @ClassName
 * @Description 盘点单控制层
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */

@Api(tags = "盘点单", description = "仓库库存盘点")
@RestController
@RequestMapping("/bill/inventory")
public class BillInventoryController {

    @Autowired
    private BillInventoryServiceImpl billInventoryService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "inventory:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Valid BillInventoryAndDetailsVo billDto) throws Exception {
        Bill bill = billDto.getBill();
        billValidationServiceImpl.valiBusinessTime(bill.getBusinessTime());
        return billInventoryService.addData(billDto);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "inventory:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return billInventoryService.cancle(billId);
    }

    @ApiOperation(value = "获取单据编号")
    @ApiPermission(value = "inventory:add", pname = "新增")
    @PostMapping("/getBillNo")
    public Result getBillNo() throws Exception {
        return billInventoryService.getBillNo();
    }

    @ApiOperation(value = "根据单据Id，获取单据和单据明细")
    @ApiPermission(value = "inventory:list", pname = "查询")
    @PostMapping("/getBillAndDetails/{billId}")
    public Result getBillAndDetailsByBillId(@PathVariable Long billId) throws Exception {
        return Result.ok(billInventoryService.getDataByBillId(billId));
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "inventory:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<BillInventory> pageParams) throws Exception {
        return billInventoryService.listPage(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "inventory:export", pname = "作废")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<BillInventory> pageParams) throws Exception {
        return billInventoryService.listPage(pageParams);
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "inventory:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody Long billId) throws Exception {
        return billInventoryService.getPrintData(billId);
    }
}
