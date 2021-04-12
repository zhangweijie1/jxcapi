package com.mrguo.controller.sys;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.dto.setup.Origin;
import com.mrguo.entity.origin.OriginMaster;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.origin.OriginMasterServiceImpl;
import com.mrguo.service.impl.origin.OriginInfoServiceImpl;
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
 * @Description 期初信息
 * @date 2020/6/1511:20 AM
 * @updater 郭成兴
 * @updatedate 2020/6/1511:20 AM
 */

@Api(tags = "结存管理")
@RestController
@RequestMapping("/origin")
public class OriginController {

    @Autowired
    private OriginInfoServiceImpl originService;
    @Autowired
    private OriginMasterServiceImpl originMasterService;

    @ApiOperation(value = "结账")
    @ApiPermission(value = "balance:do", pname = "结账")
    @PostMapping("/balance")
    public Result balance(@RequestBody OriginMaster originMaster) throws Exception {
        return originMasterService.balance(originMaster);
    }

    @ApiOperation(value = "反结账")
    @ApiPermission(value = "balance:undo", pname = "反结账")
    @PostMapping("/unbalance")
    public Result unbalance() throws Exception {
        return originMasterService.unbalance();
    }

    @ApiOperation(value = "获取结账时间")
    @PostMapping("/getBalanceTime")
    public Result getBalanceTime() throws Exception {
        return originMasterService.getBalanceTime();
    }

    @ApiOperation(value = "获取结账记录")
    @ApiPermission(value = "balance:list", pname = "查询")
    @PostMapping("/listOriginPage")
    public Result listOriginPage(@RequestBody PageParams<OriginMaster> pageParams) throws Exception {
        return originMasterService.listOriginPage(pageParams);
    }

    @ApiOperation(value = "期初商品库存")
    @ApiPermission(value = "origin:list", pname = "查询")
    @PostMapping("/listStockPage")
    public Result listStock(@RequestBody PageParams<Origin> pageParams) throws Exception {
        return originService.listStock(pageParams);
    }

    @ApiOperation(value = "期初应收欠款")
    @ApiPermission(value = "origin:list", pname = "查询")
    @PostMapping("/listDeptRecePage")
    public Result listDeptRece(@RequestBody PageParams<Customer> pageParams) throws Exception {
        return originService.listDeptRece(pageParams);
    }

    @ApiOperation(value = "期初应付欠款")
    @ApiPermission(value = "origin:list", pname = "查询")
    @PostMapping("/listDeptPayPage")
    public Result listDeptPay(@RequestBody PageParams<Supplier> pageParams) throws Exception {
        return originService.listDeptPay(pageParams);
    }

    @ApiOperation(value = "期初账户余额")
    @ApiPermission(value = "origin:list", pname = "查询")
    @PostMapping("/listAccountOriginPage")
    public Result listAccountOrigin(@RequestBody PageParams<Account> pageParams) throws Exception {
        return originService.listAccountOrigin(pageParams);
    }
}
