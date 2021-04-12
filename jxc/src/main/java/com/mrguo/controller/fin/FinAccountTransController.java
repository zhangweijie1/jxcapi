package com.mrguo.controller.fin;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.fin.FinAccountTrans;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.fin.FinAccountTransServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/3/219:29 AM
 * @updater 郭成兴
 * @updatedate 2020/3/219:29 AM
 */
@Api(tags = "账户转账")
@RestController
@RequestMapping("/fin/accountTrans")
public class FinAccountTransController {

    @Autowired
    private FinAccountTransServiceImpl finAccountTransService;

    @ApiOperation(value = "新增单据")
    @ApiPermission(value = "account_trans:add", pname = "新增")
    @PostMapping("/addData")
    public Result addData(@RequestBody @Valid FinAccountTrans finAccountTrans) throws Exception {
        return finAccountTransService.addData(finAccountTrans);
    }

    @ApiOperation(value = "作废单据")
    @ApiPermission(value = "account_trans:cancle", pname = "作废")
    @PostMapping("/cancle")
    public Result cancle(@RequestBody Long billId) throws Exception {
        return finAccountTransService.cancelTransBill(billId);
    }

    @ApiOperation(value = "分页查询单据")
    @ApiPermission(value = "account_trans:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<FinAccountTrans> pageParams) throws Exception {
        return finAccountTransService.listPage(pageParams);
    }

    @ApiOperation(value = "根据ID，查询详情")
    @PostMapping("/getData/{id}")
    public Result getDataById(@PathVariable Long id) throws Exception {
        return Result.ok(finAccountTransService.getDataById(id));
    }
}
