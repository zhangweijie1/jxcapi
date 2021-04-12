package com.mrguo.dto.sys;

import com.mrguo.entity.sys.SysPermission;
import com.mrguo.entity.sys.SysRole;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/192:34 PM
 * @updater 郭成兴
 * @updatedate 2019/4/192:34 PM
 */
@Data
public class RoleDto {
    private SysRole sysRole;
    private List<SysPermission> permissionList;
}
