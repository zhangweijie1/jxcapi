package com.mrguo.controller.basedata.goods;


import com.mrguo.common.entity.Result;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import com.mrguo.interfaces.ApiPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mrguo.service.inter.bsd.GoodsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 后台管理商品Controller
 *
 * @author mrguo
 */
@Api(tags = "商品")
@RestController
@RequestMapping("/good")
public class GoodContrller {

    @Autowired
    private GoodsService goodsService;

    /**
     * 添加商品
     *
     * @param goodsAssemblyVo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "组合新增商品")
    @ApiPermission(value = "good:adedit", pname = "新增编辑")
    @PostMapping("/addAssemblyData")
    public Result save(@RequestBody @Validated GoodsAssemblyVo goodsAssemblyVo) throws Exception {
        return goodsService.addGoodAssemblyData(goodsAssemblyVo);
    }

    @ApiOperation(value = "获取商品导入模板")
    @PostMapping("/getTemplate")
    public Result getTemplate(@RequestBody Map map) throws Exception {
        Integer specsQty = (Integer) map.get("specsQty");
        Integer unitQty = (Integer) map.get("unitQty");
        return goodsService.createExcleTemp(specsQty, unitQty);
    }

    @ApiOperation(value = "导出商品")
    @ApiPermission(value = "good:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody Long storeId) throws Exception {
        return goodsService.exportData(storeId);
    }

    @ApiOperation(value = "导入商品")
    @ApiPermission(value = "good:import", pname = "导入")
    @PostMapping("/import")
    public Result importData(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        Workbook workbook = Workbook.getWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheet(0);
        return goodsService.importData(sheet);
    }
}
