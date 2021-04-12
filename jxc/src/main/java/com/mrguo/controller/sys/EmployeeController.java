package com.mrguo.controller.sys;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dto.sys.EmployeeDto;
import com.mrguo.entity.sys.SysEmployee;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysEmployeeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/2710:30 AM
 * @updater 郭成兴
 * @updatedate 2019/12/2710:30 AM
 */

@Api(tags = "员工管理")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private SysEmployeeServiceImpl sysEmployeeService;

    @ApiOperation(value = "新增或编辑员工")
    @ApiPermission(value = "employee:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody EmployeeDto employeeDto) throws Exception {
        return sysEmployeeService.addOrUpdateData(employeeDto);
    }

    @ApiOperation(value = "删除员工")
    @ApiPermission(value = "employee:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result deleteData(@PathVariable Long id) throws Exception {
        return sysEmployeeService.delData(id);
    }

    @ApiOperation(value = "编辑员工")
    @ApiPermission(value = "employee:adedit", pname = "新增编辑")
    @PostMapping("/updateData")
    public Result updateData(@RequestBody SysEmployee employee) throws Exception {
        return Result.ok(sysEmployeeService.updateData(employee));
    }

    @ApiOperation(value = "获取员工详情")
    @ApiPermission(value = "employee:list", pname = "查询")
    @PostMapping("/getData/{id}")
    public Result getdataById(@PathVariable Long id) throws Exception {
        return sysEmployeeService.getDataById(id);
    }

    @ApiOperation(value = "分页查询员工")
    @ApiPermission(value = "employee:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<SysEmployee> pageParams) throws Exception {
        return sysEmployeeService.listPage(pageParams);
    }

    @ApiOperation(value = "获取员工下拉框", notes = "不包含导购")
    @PostMapping("/listOptions")
    public Result optlistOptionsions() throws Exception {
        return sysEmployeeService.listOptions();
    }

    @ApiOperation(value = "获取员工下拉框", notes = "包含导购")
    @PostMapping("/listOptionsIncludeGuide")
    public Result getOptionsIncludeGuide() throws Exception {
        return sysEmployeeService.listOptionsIncludeGuide();
    }

    @ApiOperation(value = "获取员工下拉框", notes = "根据当前登录人获取")
    @PostMapping("/listOptionsByCurrentUser")
    public Result optionsCurrent() throws Exception {
        return sysEmployeeService.listOptionsByCurrentUser();
    }
}
