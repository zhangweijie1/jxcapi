package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("sysUserRoleMapper")
public interface SysUserRoleMapper extends MyMapper<SysUserRole> {

    @Delete("delete from sys_user_role where user_id = #{userId}")
    int delRolesByUserId(@Param("userId")Long userId);
}