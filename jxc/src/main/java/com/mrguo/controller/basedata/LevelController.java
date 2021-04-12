package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Level;
import com.mrguo.service.impl.basedata.LevelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName: LevelController
 * @Description: 客户等级
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/5 8:30 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@Api(tags = "客户等级")
@RestController
@RequestMapping("/level")
public class LevelController {

    @Autowired
    private LevelServiceImpl levelService;

    @ApiOperation(value = "新增或编辑等级")
    @PostMapping("/saveData")
    public Result save(@RequestBody @Valid Level level) throws Exception {
        return levelService.addOrUpdateData(level);
    }

    @ApiOperation(value = "删除等级")
    @PostMapping("/delData/{levelId}")
    public Result delData(@PathVariable Long levelId) throws Exception {
        return levelService.delData(levelId);
    }

    @ApiOperation(value = "分页查询等级")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<Level> pageParams) throws Exception {
        return levelService.listPage(pageParams);
    }

    @ApiOperation(value = "查询所有等级")
    @PostMapping("/listAllData")
    public Result listAllData() throws Exception {
        return levelService.listAllData();
    }

    @ApiOperation(value = "查询等级下拉框")
    @PostMapping("/listOptions")
    public Result listOptions() throws Exception {
        return levelService.getOptions();
    }
}
