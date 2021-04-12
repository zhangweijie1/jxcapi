package com.mrguo.dto.sys;

import com.mrguo.entity.sys.SysEmployee;
import com.mrguo.entity.sys.SysPermissionData;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/2710:14 AM
 * @updater 郭成兴
 * @updatedate 2019/12/2710:14 AM
 */
@Data
public class EmployeeDto {

    /**
     * 员工
     */
    private SysEmployee employee;

    /**
     * 角色
     */
    private List<String> roleList;

    /**
     * 数据权限
     */
    private SysPermissionData permissionData;
}
