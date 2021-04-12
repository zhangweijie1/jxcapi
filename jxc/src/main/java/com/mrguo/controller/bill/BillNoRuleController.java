package com.mrguo.controller.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.sys.SysBillNoRule;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.BillNoRuleServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/153:46 PM
 * @updater 郭成兴
 * @updatedate 2020/6/153:46 PM
 */

@Api(tags = "单据编号规则", description = "单据编号前缀，只能修改查询")
@RestController
@RequestMapping("/bill/noRule")
public class BillNoRuleController {

    @Autowired
    private BillNoRuleServiceImpl billNoPreService;

    @ApiOperation(value = "修改规则")
    @ApiPermission(value = "billno_rule:update", pname = "编辑")
    @PostMapping("/updateData")
    public Result updateData(@RequestBody SysBillNoRule billNoRule) throws Exception {
        return Result.ok(billNoPreService.updateData(billNoRule));
    }

    @ApiOperation(value = "根据Id，获取规则明细")
    @ApiPermission(value = "billno_rule:update", pname = "编辑")
    @PostMapping("/getData/{id}")
    public Result getDataById(@PathVariable Long id) throws Exception {
        return billNoPreService.getDataById(id);
    }

    @ApiOperation(value = "分页查询规则")
    @ApiPermission(value = "billno_rule:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<SysBillNoRule> pageParams) throws Exception {
        Page<SysBillNoRule> billNoRulePage = billNoPreService.listPage(pageParams);
        List<SysBillNoRule> records = billNoRulePage.getRecords();
        for (SysBillNoRule billNoRule : records) {
            billNoRule.setBillCatName(BillCatEnum.getMessageByCode(billNoRule.getBillCat()));
        }
        return Result.ok(billNoRulePage);
    }
}
