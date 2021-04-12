package com.mrguo.controller.basedata;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Specs;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.SpecsServiceImpl;
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
 * @Description 规格管理
 * @date 2020/6/1110:23 AM
 * @updater 郭成兴
 * @updatedate 2020/6/1110:23 AM
 */
@Api(tags = "规格管理")
@RestController
@RequestMapping("/specs")
public class SpecsController {

    @Autowired
    private SpecsServiceImpl specsService;

    @ApiOperation(value = "新增或编辑规格")
    @ApiPermission(value = "specs:adedit", pname = "新增编辑")
    @PostMapping("/save")
    public Result save(@RequestBody @Valid Specs specs) throws Exception {
        return specsService.saveOrUpdateData(specs) == null
                ? Result.fail("保存失败！")
                : Result.ok(specs);
    }

    @ApiOperation(value = "删除规格")
    @ApiPermission(value = "specs:del", pname = "删除")
    @PostMapping("/del")
    public Result del(@RequestBody Long id) throws Exception {
        return specsService.delData(id);
    }

    @ApiOperation(value = "查询所有规格")
    @ApiPermission(value = "specs:list", pname = "查询")
    @PostMapping("/listAllData")
    public Result listAllData() throws Exception {
        return Result.ok(specsService.selectAll());
    }
}
