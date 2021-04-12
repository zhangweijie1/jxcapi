package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.SupplierServiceImpl;
import com.mrguo.util.business.OrderNoGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 后台管理供应商Controller
 *
 * @author mrguo
 */
@Api(tags = "供应商管理")
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierServiceImpl supplierService;
    @Autowired
    private OrderNoGenerator orderNoGenerator;

    @ApiOperation(value = "新增或保存")
    @ApiPermission(value = "supplier:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody @Valid Supplier supplier) throws Exception {
        supplierService.saveOrUpdateData(supplier);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @ApiPermission(value = "supplier:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        return supplierService.delDataById(id);
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "supplier:list", pname = "查询")
    @PostMapping("/listPage")
    public Result list(@RequestBody PageParams<Supplier> pageParams) throws Exception {
        return supplierService.listPage(pageParams);
    }

    @ApiOperation(value = "查询下拉数据")
    @PostMapping("/listOptions")
    public Result getOptions(@RequestBody(required = false) String keywords) throws Exception {
        return supplierService.listOptions(keywords);
    }

    @ApiOperation(value = "查询详情byId")
    @PostMapping("/getData/{id}")
    public Result getDataById(@PathVariable Long id) throws Exception {
        return supplierService.getDataById(id);
    }

    @ApiOperation(value = "查询该供应商欠款")
    @PostMapping("/getDebt/{id}")
    public Result getDetById(@PathVariable Long id) throws Exception {
        return supplierService.getDebtById(id);
    }

    @ApiOperation(value = "下拉框模糊查询")
    @PostMapping("/comboList")
    @ResponseBody
    public Result comboList(@RequestBody Map map) throws Exception {
        String q = (String) map.get("keywords");
        if (q == null) {
            q = "";
        }
        return supplierService.findByName("%" + q + "%");
    }

    @ApiOperation(value = "统计")
    @PostMapping("/statistics")
    public Result statistics(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return supplierService.statistics(pageParams);
    }

    @ApiOperation(value = "获取编号")
    @PostMapping("/getNo")
    public Result getCode() throws Exception {
        return supplierService.getCode();
    }

    @ApiOperation(value = "获取倒入模板")
    @PostMapping("/downloadExcleTemp")
    public Result downloadExcleTemp() throws Exception {
        return supplierService.createExcleTemp();
    }

    @ApiOperation(value = "导入数据")
    @PostMapping("/import")
    public Result importData() throws Exception {
        return supplierService.createExcleTemp();
    }

    @ApiOperation(value = "导出数据")
    @PostMapping("/export")
    public Result exportData() throws Exception {
        return supplierService.exportData();
    }
}
