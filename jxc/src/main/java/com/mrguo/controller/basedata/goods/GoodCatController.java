package com.mrguo.controller.basedata.goods;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.goods.Goodscat;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.inter.bsd.GoodscatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 后台管理商品类别Controller
 *
 * @author mrguo
 */
@Api(tags = "商品分类")
@RestController
@RequestMapping("/goodCat")
public class GoodCatController {

    @Autowired
    private GoodscatService goodscatService;

    @ApiOperation(value = "新增或保存", notes = "")
    @ApiPermission(value = "goodcat:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody @Valid Goodscat goodscat) throws Exception {
        return goodscatService.addOrUpdateData(goodscat);
    }

    @ApiOperation(value = "删除", notes = "")
    @ApiPermission(value = "goodcat:del", pname = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody String id) throws Exception {
        return goodscatService.delete(id);
    }

    @ApiOperation(value = "获取所有分类", notes = "")
    @PostMapping("/listAllData")
    public Result listAllData() throws Exception {
        return Result.ok(goodscatService.listAllData());
    }
}
