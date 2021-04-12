package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.interfaces.ApiPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mrguo.entity.bsd.Customer;
import com.mrguo.service.inter.bsd.ComegoService;

import javax.validation.Valid;

/**
 * 后台管理客户Controller
 *
 * @author mrguo
 */
@Api(tags = "客户")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ComegoService comegoService;

    @ApiOperation(value = "新增或保存", notes = "")
    @ApiPermission(value = "customer:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result addData(@RequestBody @Valid Customer customer) throws Exception {
        return comegoService.saveOrUpdateData(customer);
    }

    @ApiOperation(value = "删除", notes = "")
    @ApiPermission(value = "customer:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        return comegoService.deleteById(id);
    }

    @ApiOperation(value = "分页查询", notes = "")
    @ApiPermission(value = "customer:list", pname = "查询")
    @PostMapping("/listPage")
    public Result list(@RequestBody PageParams<Customer> pageParams) throws Exception {
        return comegoService.listPage(pageParams);
    }

    @ApiOperation(value = "查询下拉数据", notes = "")
    @PostMapping("/listOptions")
    public Result getOptions(@RequestBody(required = false) String keywords) throws Exception {
        return comegoService.listOptions(keywords);
    }

    @ApiOperation(value = "根据Id，获取详情", notes = "")
    @ApiPermission(value = "customer:list", pname = "查询")
    @PostMapping("/getData/{id}")
    public Result getDataById(@PathVariable Long id) throws Exception {
        return comegoService.getDataById(id);
    }

    @ApiOperation(value = "根据Id,获取该客户的欠款额", notes = "")
    @PostMapping("/getDebt/{id}")
    public Result getDebtById(@PathVariable Long id) throws Exception {
        return comegoService.getDebtById(id);
    }

    @ApiOperation(value = "根据名字查询", notes = "")
    @PostMapping("/listDataByName")
    public Result listDataByName(String q) throws Exception {
        if (q == null) {
            q = "";
        }
        return comegoService.listDataByName("%" + q + "%");
    }

    @ApiOperation(value = "获取编号", notes = "")
    @ApiPermission(value = "customer:adedit", pname = "新增编辑")
    @PostMapping("/getNo")
    public Result getNo() throws Exception {
        return comegoService.getCode();
    }

    @PostMapping("/downloadExcleTemp")
    public Result downloadExcleTemp() throws Exception {
        return comegoService.getExcleTemp();
    }

    @PostMapping("/import")
    @ResponseBody
    public Result importData() throws Exception {
        return comegoService.getExcleTemp();
    }

    @PostMapping("/export")
    public Result exportData() throws Exception {
        return comegoService.exportData();
    }
}
