package com.mrguo.service.impl.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.sys.SysUserRoleMapper;
import com.mrguo.entity.sys.SysUser;
import com.mrguo.entity.sys.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/10/14:14 PM
 * @updater 郭成兴
 * @updatedate 2019/10/14:14 PM
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole> {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public MyMapper<SysUserRole> getMapper() {
        return sysUserRoleMapper;
    }

}
