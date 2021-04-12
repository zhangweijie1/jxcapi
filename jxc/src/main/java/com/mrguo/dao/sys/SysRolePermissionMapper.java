package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysRole;
import com.mrguo.entity.sys.SysRolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("sysRolePermissionMapper")
public interface SysRolePermissionMapper extends MyMapper<SysRolePermission> {
    
    /**
     * 查询权限 by roles
     * @param 
     * @return  
     * @throws  
     * @author 郭成兴
     * @createdate 2020/7/23 12:43 PM
     * @updater 郭成兴
     * @updatedate 2020/7/23 12:43 PM
     */
    List<SysRolePermission> selectListByRoleIds(List<String> roleIds);
}