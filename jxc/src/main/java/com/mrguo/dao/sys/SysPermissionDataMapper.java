package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysPermissionData;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("sysPermissionDataMapper")
public interface SysPermissionDataMapper extends MyMapper<SysPermissionData> {
}