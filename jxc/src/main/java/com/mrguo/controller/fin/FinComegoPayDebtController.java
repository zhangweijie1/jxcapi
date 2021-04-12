package com.mrguo.controller.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.fin.FinComegoPayDebtServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author 郭成兴
 * @ClassName FinComegoPayDebtController
 * @Description 应付欠款
 * @date 2020/3/154:04 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:04 PM
 */
@Api(tags = "应付欠款（ 供应商 ）")
@RestController
@RequestMapping("/fin/comegoPayDebt")
public class FinComegoPayDebtController {

    @Autowired
    private FinComegoPayDebtServiceImpl finComegoPayDebtService;

    @ApiOperation(value = "查询某供应商，应付欠款明细")
    @ApiPermission(value = "debt_pay:list", pname = "查询")
    @PostMapping("/listDebtDetail/{comegoId}")
    public Result listDebtDetailDataByComegoId(@PathVariable Long comegoId,
                                                               @RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        Map<String, Object> data = pageParams.getData();
        data.put("comegoId", comegoId);
        return finComegoPayDebtService.listDebtDetailByComegoId(pageParams);
    }

    @ApiOperation(value = "分页查询应付欠款，按供应商列表查询")
    @ApiPermission(value = "debt_pay:list", pname = "查询")
    @PostMapping("/listDataGroupByComegoPage")
    public Result listDataGroupByComegoId(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return finComegoPayDebtService.listDataGroupByComegoPage(pageParams);
    }

    @ApiOperation(value = "统计供应商应付欠款")
    @ApiPermission(value = "debt_pay:list", pname = "查询")
    @PostMapping("/getStatistics")
    public Result getStatistics(@RequestBody Map<String, Object> data) throws Exception {
        return Result.ok(finComegoPayDebtService.getStatistics(data));
    }

    @ApiOperation(value = "导出")
    @ApiPermission(value = "debt_pay:export", pname = "导出")
    @PostMapping("/export")
    public IPage<FinBill> export(@RequestBody PageParams<FinBill> pageParams) throws Exception {
        return null;
    }
}
