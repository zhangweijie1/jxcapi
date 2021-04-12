package com.mrguo.dao.sys;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysPermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName SysPermissionMapper
 * @Description
 * @date 2019/5/5 10:09 AM
 * @updater 郭成兴
 * @updatedate 2019/5/5 10:09 AM
 */
@Repository("sysPermissionMapper")
public interface SysPermissionMapper extends MyMapper<SysPermission> {
    /**
     * 查询所有的resource
     */
    List<String> selectAllResource();

    /**
     * 删除所有的
     *
     * @return
     */
    @Delete("delete from sys_permission")
    int deleteAll();

    @Override
    @Select("select DISTINCT per_id, per_name, per_parent, priority from sys_permission\n" +
            "order by priority asc")
    List<SysPermission> selectAll();

    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}