package com.mrguo.controller.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.fin.FinPayDailyOutServiceImpl;
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
 * @Description 日常支出
 * @date 2020/3/154:04 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:04 PM
 */
@Api(tags = "日常支出")
@RestController
@RequestMapping("/fin/dailyPay")
public class FinPayDailyOutController {

    @Autowired
    private FinPayDailyOutServiceImpl finPayDailyOutService;

    @ApiOperation(value = "新增支出单")
    @ApiPermission(value = "dayily:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Valid FinBill bill) throws Exception {
        return finPayDailyOutService.addDayilyOutData(bill);
    }

    @ApiOperation(value = "作废支出单")
    @ApiPermission(value = "dayily:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws CustomsException {
        return finPayDailyOutService.cancleBill(billId);
    }

    @ApiOperation(value = "获取编号")
    @PostMapping("/getBillNo")
    public Result getBillCode() throws CustomsException {
        return finPayDailyOutService.getBillCode();
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "dayily:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<FinBill> pageParams) {
        return finPayDailyOutService.listDayilyOutData(pageParams);
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "dayily:export", pname = "导出")
    @PostMapping("/export")
    public Result export() {
        return finPayDailyOutService.export();
    }

    @ApiOperation(value = "打印")
    @ApiPermission(value = "dayily:print", pname = "打印")
    @PostMapping("/print")
    public Result print(@RequestBody PageParams<FinBill> pageParams) {
        return null;
    }
}
