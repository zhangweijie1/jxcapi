package com.mrguo.controller.sys;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.sys.SysEmployee;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysGuideServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 导购员
 * @date 2019/12/2710:30 AM
 * @updater 郭成兴
 * @updatedate 2019/12/2710:30 AM
 */

@Api(tags = "导购员管理")
@RestController
@RequestMapping("/guide")
public class GuideController {

    @Autowired
    private SysGuideServiceImpl sysGuideService;

    @ApiOperation(value = "新增或编辑")
    @ApiPermission(value = "employee:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody SysEmployee employee) throws Exception {
        return sysGuideService.addOrUpdateData(employee);
    }

    @ApiOperation(value = "删除导购")
    @ApiPermission(value = "employee:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result deleteListData(@PathVariable Long id) throws Exception {
        return sysGuideService.delDataById(id);
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "employee:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<SysEmployee> pageParams) throws Exception {
        return sysGuideService.listGuidePage(pageParams);
    }
}
