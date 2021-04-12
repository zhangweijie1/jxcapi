package com.mrguo.controller.sys;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dto.sys.RoleDto;
import com.mrguo.entity.sys.SysRole;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/192:32 PM
 * @updater 郭成兴
 * @updatedate 2019/4/192:32 PM
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @ApiOperation(value = "新增或保存", notes = "")
    @ApiPermission(value = "role:adedit", pname = "新增编辑")
    @PostMapping(value = "/saveData")
    public Result saveData(@RequestBody SysRole role) throws CustomsException {
        return sysRoleService.saveOrUpdateData(role);
    }

    @ApiOperation(value = "删除角色")
    @ApiPermission(value = "role:del", pname = "删除")
    @PostMapping(value = "/delListData")
    public Result delData(@RequestBody List<Long> ids) throws CustomsException {
        return sysRoleService.delListData(ids);
    }

    @ApiOperation(value = "修改角色权限")
    @PostMapping(value = "/updatePermissions")
    public Result updatePermissionsData(@RequestBody RoleDto roleDto) throws Exception {
        return sysRoleService.updateRolePermissionsData(roleDto.getSysRole(), roleDto.getPermissionList());
    }

    @ApiOperation(value = "分页查询")
    @ApiPermission(value = "role:list", pname = "查询")
    @PostMapping(value = "/listPage")
    public Result listData(@RequestBody PageParams<SysRole> pageParams) throws Exception {
        return sysRoleService.listPage(pageParams);
    }

    @ApiOperation(value = "获取下拉数据")
    @PostMapping(value = "/listOptions")
    public Result listOptions() throws Exception {
        return sysRoleService.listOptions();
    }
}
