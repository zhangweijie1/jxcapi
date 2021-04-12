package com.mrguo.entity.sys;

import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/2312:53 PM
 * @updater 郭成兴
 * @updatedate 2020/7/2312:53 PM
 */

@Data
public class RoleAndPermissions {

    List<SysRole> roleList;

    List<SysRolePermission> rolePermissionList;
}
