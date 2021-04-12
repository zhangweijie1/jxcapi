package com.mrguo.controller.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.fin.FinCapitalCat;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.fin.FinCapitalCatServiceImpl;
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
 * @ClassName FinComegoController
 * @Description 账目类型
 * @date 2020/3/154:04 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:04 PM
 */

@Api(tags = "账目类型")
@RestController
@RequestMapping("/fin/capitalCat")
public class FinCapitalCatController {

    @Autowired
    private FinCapitalCatServiceImpl finCapitalCatService;

    @ApiOperation(value = "新增或者编辑")
    @ApiPermission(value = "dayily:add", pname = "新增")
    @PostMapping("/saveData")
    public Result saveOrUpdateData(@RequestBody @Valid FinCapitalCat finCapitalCat) throws Exception {
        if (finCapitalCat.getId() == null) {
            return Result.ok(finCapitalCatService.saveData(finCapitalCat));
        } else {
            return Result.ok(finCapitalCatService.updateData(finCapitalCat));
        }
    }

    @ApiOperation(value = "删除")
    @ApiPermission(value = "dayily:add", pname = "新增")
    @PostMapping("/delData")
    public Result delData(@RequestBody Long id) throws Exception {
        return finCapitalCatService.delData(id);
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "dayily:list", pname = "查询")
    @PostMapping("/listPage")
    public Result getListData(@RequestBody PageParams<FinCapitalCat> pageParams) throws Exception {
        return finCapitalCatService.listPage(pageParams);
    }

    @ApiOperation(value = "获取支出下拉数据")
    @PostMapping("/listPayOptions")
    public Result listPayOptions() throws Exception {
        return Result.ok(finCapitalCatService.getPayOptions());
    }

    @ApiOperation(value = "获取收入下拉数据")
    @PostMapping("/listReceOptions")
    public Result listReceOptions() throws Exception {
        return Result.ok(finCapitalCatService.getReceOptions());
    }
}
