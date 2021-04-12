package com.mrguo.service.impl.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.sys.SysRolePermissionMapper;
import com.mrguo.entity.sys.SysRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/192:02 PM
 * @updater 郭成兴
 * @updatedate 2019/4/192:02 PM
 */
@Service
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermission> {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public MyMapper<SysRolePermission> getMapper() {
        return sysRolePermissionMapper;
    }
}
