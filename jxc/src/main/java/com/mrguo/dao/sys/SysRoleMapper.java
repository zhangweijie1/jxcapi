package com.mrguo.dao.sys;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.sys.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author 郭成兴
 * @ClassName SysRoleMapper
 * @Description 权限DAO
 * @date 2019/4/14 11:42 AM
 * @updater 郭成兴
 * @updatedate 2019/4/14 11:42 AM
 */
@Repository("sysRoleMapper")
public interface SysRoleMapper extends MyMapper<SysRole> {
    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<String> selectRoleByUserId(String userId);

    /**
     * 分页查询role
     *
     * @param page
     * @param data
     * @return
     */
    List<SysRole> listPage(Page<SysRole> page,
                           @Param("record") Map<String, Object> data);

    /**
     * 获取roles下拉框
     *
     * @return
     */
    @Select("select * from sys_role")
    List<SysRole> listOptions();

}