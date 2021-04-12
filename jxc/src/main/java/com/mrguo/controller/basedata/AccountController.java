package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Account;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
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
 * @ClassName
 * @Description
 * @date 2019/12/183:14 PM
 * @updater 郭成兴
 * @updatedate 2019/12/183:14 PM
 */
@Api(tags = "账户")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @ApiOperation(value = "新增或保存账户")
    @ApiPermission(value = "account:adedit", pname = "新增编辑")
    @PostMapping("/save")
    public Result save(@RequestBody @Valid Account account) throws Exception {
        return Result.ok(accountService.addOrUpdateData(account));
    }

    @ApiOperation(value = "删除账户")
    @ApiPermission(value = "account:del", pname = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long accountId) throws Exception {
        return accountService.delAccountData(accountId);
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "account:list", pname = "查询")
    @PostMapping("/listPage")
    public Result list(@RequestBody PageParams<Account> pageParams) throws Exception {
        return accountService.listPage(pageParams);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("/listOptions")
    public Result listOptions() throws Exception {
        return Result.ok(accountService.listOptions());
    }
}
