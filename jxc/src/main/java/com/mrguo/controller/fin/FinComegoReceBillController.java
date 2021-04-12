package com.mrguo.controller.fin;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.fin.FinComegoReceBillServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author 郭成兴
 * @ClassName FinComegoController
 * @Description 往来账 - 应收欠款
 * @date 2020/3/154:04 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:04 PM
 */
@Api(tags = "收款单（往来单位）")
@RestController
@RequestMapping("/fin/comegoReceBill")
public class FinComegoReceBillController {

    @Autowired
    private FinComegoReceBillServiceImpl finComegoReceiptService;

    @ApiOperation(value = "新增收款单")
    @ApiPermission(value = "debt_rece:add", pname = "新增")
    @PostMapping("/addData")
    public Result addReceiptData(@RequestBody @Valid FinBill bill) throws Exception {
        return finComegoReceiptService.addReceData(bill);
    }

    @ApiOperation(value = "作废收款单")
    @ApiPermission(value = "debt_rece:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancleBill(@RequestBody Long billId) throws Exception {
        return finComegoReceiptService.cancleBill(billId);
    }

    @ApiOperation(value = "获取编号")
    @ApiPermission(value = "debt_rece:add", pname = "新增")
    @PostMapping("/getBillNo")
    public Result getBillCode() throws Exception {
        return finComegoReceiptService.getBillCode();
    }
}
