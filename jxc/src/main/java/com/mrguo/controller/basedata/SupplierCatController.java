package com.mrguo.controller.basedata;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.SupplierCat;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.SupplierCatServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 后台管理商品类别Controller
 *
 * @author mrguo
 */
@Api(tags = "供应商分类")
@RestController
@RequestMapping("/supplierCat")
public class SupplierCatController {

    @Resource
    private SupplierCatServiceImpl supplierCatService;

    @ApiOperation(value = "查询所有分类")
    @ApiPermission(value = "suppliercat:list", pname = "查询")
    @PostMapping("/listAllData")
    public Result loadTreeInfo() throws Exception {
        return Result.ok(supplierCatService.getAllData());
    }

    @ApiOperation(value = "新增或保存分类")
    @PostMapping("/saveData")
    @ApiPermission(value = "suppliercat:adedit", pname = "新增编辑")
    public Result saveData(@RequestBody @Valid SupplierCat supplierCat) throws Exception {
        return supplierCatService.addOrUpdateData(supplierCat);
    }

    @ApiOperation(value = "删除")
    @ApiPermission(value = "suppliercat:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable String id) throws Exception {
        supplierCatService.delete(id);
        return Result.ok();
    }

}
