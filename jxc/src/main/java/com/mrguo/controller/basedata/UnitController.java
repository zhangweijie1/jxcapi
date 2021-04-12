package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Unit;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.UnitServiceImpl;
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
 * @date 2019/12/1011:21 AM
 * @updater 郭成兴
 * @updatedate 2019/12/1011:21 AM
 */
@Api(tags = "计量单位管理")
@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitServiceImpl unitService;

    @ApiOperation(value = "新增或保存")
    @ApiPermission(value = "unit:adedit", pname = "新增编辑")
    @PostMapping("/save")
    public Result save(@RequestBody @Valid Unit unit) throws Exception {
        return unitService.saveOrUpdateData(unit);
    }

    @ApiOperation(value = "删除")
    @ApiPermission(value = "unit:del", pname = "删除")
    @PostMapping("/del")
    public Result delData(@RequestBody Long id) throws Exception {
        return unitService.delData(id);
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "unit:list", pname = "查询")
    @PostMapping("/listPage")
    public Result list(@RequestBody PageParams<Unit> pageParams) throws Exception {
        return unitService.listPage(pageParams);
    }

    @ApiOperation(value = "查询下拉框")
    @ApiPermission(value = "unit:list", pname = "查询")
    @PostMapping("/listOptions")
    public Result listOptions() throws Exception {
        return unitService.listOptions();
    }
}
