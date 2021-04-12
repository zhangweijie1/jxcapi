package com.mrguo.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.common.utils.MapToEntityUtil;
import com.mrguo.dao.sys.SysEmployeeMapper;
import com.mrguo.dao.sys.SysPermissionDataMapper;
import com.mrguo.dao.sys.SysUserRoleMapper;
import com.mrguo.dto.sys.EmployeeDto;
import com.mrguo.entity.sys.*;
import com.mrguo.util.enums.ElmType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/2710:03 AM
 * @updater 郭成兴
 * @updatedate 2019/12/2710:03 AM
 */
@Service("sysEmployeeServiceImpl")
public class SysEmployeeServiceImpl extends BaseServiceImpl<SysEmployee> {

    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysPermissionDataMapper permissionDataMapper;
    @Autowired
    private SysUserServiceImpl userService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<SysEmployee> getMapper() {
        return sysEmployeeMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addOrUpdateData(EmployeeDto employeeDto) throws CustomsException {
        SysEmployee employee = employeeDto.getEmployee();
        if (employee.getId() != null) {
            return updateData(employeeDto);
        } else {
            return addData(employeeDto);
        }
    }

    private Result addData(EmployeeDto employeeDto) throws CustomsException {
        SysEmployee sysEmployee = employeeDto.getEmployee();
        List<String> roleList = employeeDto.getRoleList();
        SysPermissionData permissionData = employeeDto.getPermissionData();
        // 1 新增 user用户表
        SysUser sysUser = new SysUser();
        sysUser.setUsername(sysEmployee.getUsername());
        sysUser.setPassword(sysEmployee.getPassword());
        sysUser = sysUserService.regist(sysUser);
        // 2 新增 employee员工表
        sysEmployee.setId(sysUser.getUid());
        addData(sysEmployee);
        // 3 新增 角色
        List<SysUserRole> userRoleList = getUserRoleList(sysUser, roleList);
        userRoleMapper.insertList(userRoleList);
        // 4 新增数据权限
        permissionData.setUserId(sysUser.getUid());
        permissionDataMapper.insert(permissionData);
        return Result.ok();
    }

    public Result addData(SysEmployee sysEmployee) {
        Date date = new Date();
        sysEmployee.setCreateTime(date);
        sysEmployee.setUpdateTime(date);
        if (sysEmployee.getStatus() == null) {
            sysEmployee.setStatus("1");
        }
        if (sysEmployeeMapper.insertSelective(sysEmployee) > 0) {
            return Result.ok();
        } else {
            return Result.fail("新增失败");
        }
    }

    public Result delData(Long id) {
        sysEmployeeMapper.deleteByPrimaryKey(id);
        sysUserService.deleteDataByKey(id);
        userRoleMapper.delRolesByUserId(id);
        if (permissionDataMapper.deleteByPrimaryKey(id) > 0) {
            return Result.ok("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    public Result updateData(EmployeeDto employeeDto) {
        SysEmployee employee = employeeDto.getEmployee();
        SysPermissionData permissionData = employeeDto.getPermissionData();
        List<String> roleList = employeeDto.getRoleList();
        Long userId = employee.getId();
        // 员工表
        if (StringUtils.isBlank(employee.getPassword())) {
            employee.setPassword(null);
        } else {
            employee.setPassword(IDUtil.encryptPassword(employee.getPassword(), employee.getUsername()));
        }
        sysEmployeeMapper.updateByPrimaryKeySelective(employee);
        // 用户表
        SysUser sysUser = new SysUser();
        sysUser.setUid(userId);
        sysUser.setUsername(employee.getUsername());
        sysUser.setPassword(employee.getPassword());
        sysUserService.updateData(sysUser);
        // 角色
        userRoleMapper.delRolesByUserId(userId);
        List<SysUserRole> userRoleList = getUserRoleList(sysUser, roleList);
        userRoleMapper.insertList(userRoleList);
        // 数据权限
        permissionData.setUserId(userId);
        permissionDataMapper.updateByPrimaryKey(permissionData);
        // 清除redis缓存
        // 1. USER_INFO = 数据权限，店铺关系
        userService.delUserInfoCacheByUserId(userId);
        // 2. 功能权限（因为切换了角色）
        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userService.delPermissionsCacheByUserIds(userIds);
        return Result.ok();
    }

    /**
     * 查询导购员
     *
     * @param pageParams
     * @return
     * @throws IOException
     */
    public Result listGuidePage(PageParams<SysEmployee> pageParams) throws IOException {
        Page<SysEmployee> page = pageParams.getPage();
        SysEmployee sysEmployee = MapToEntityUtil.map2Entity(pageParams.getData(), SysEmployee.class);
        ArrayList<String> types = new ArrayList<>();
        types.add("1");
        page.setRecords(sysEmployeeMapper.listPage(page, types, sysEmployee));
        return Result.ok(page);
    }

    public Result listPage(PageParams<SysEmployee> pageParams) throws IOException {
        Page<SysEmployee> page = pageParams.getPage();
        SysEmployee sysEmployee = MapToEntityUtil.map2Entity(pageParams.getData(), SysEmployee.class);
        ArrayList<String> types = new ArrayList<>();
        types.add("0");
        types.add("2");
        page.setRecords(sysEmployeeMapper.listPage(page, types, sysEmployee));
        return Result.ok(page);
    }

    /**
     * 获取员工下拉框（包含导购）
     *
     * @return
     */
    public Result listOptionsIncludeGuide() {
        HashMap<Object, Object> data = new HashMap<>();
        return Result.ok(sysEmployeeMapper.listOptions(data));
    }

    /**
     * 获取员工下拉框（不包含导购）
     *
     * @return
     */
    public Result listOptions() {
        HashMap<Object, Object> data = new HashMap<>();
        List<String> types = Arrays.asList("0", "2");
        data.put("types", types);
        return Result.ok(sysEmployeeMapper.listOptions(data));
    }

    /**
     * 获取当前人的options
     * 如果是管理员则是所有options
     *
     * @return
     */
    public Result listOptionsByCurrentUser() {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        Long userId = (Long) request.getAttribute("userId");
        HashMap<Object, Object> data = new HashMap<>();
        if (!ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            data.put("id", userId);
            List<SysEmployee> options = sysEmployeeMapper.listOptions(data);
            return Result.ok(options);
        }
        List<String> types = Arrays.asList("0", "2");
        data.put("types", types);
        return Result.ok(sysEmployeeMapper.listOptions(data));
    }

    /**
     * 获取员工数据：by userId
     *
     * @param userId
     * @return
     */
    public Result getDataById(Long userId) {
        SysEmployee employee = sysEmployeeMapper.getDataById(userId);
        List<String> rolesByUserId = sysEmployeeMapper.getRolesByUserId(userId);
        SysPermissionData permissionData = permissionDataMapper.selectByPrimaryKey(userId);
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployee(employee);
        employeeDto.setPermissionData(permissionData);
        employeeDto.setRoleList(rolesByUserId);
        return Result.ok(employeeDto);
    }

    private List<SysUserRole> getUserRoleList(SysUser user, List<String> roleList) {
        ArrayList<SysUserRole> result = new ArrayList<>();
        for (String roleId : roleList) {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(Long.valueOf(roleId));
            userRole.setUserId(user.getUid());
            result.add(userRole);
        }
        return result;
    }
}
