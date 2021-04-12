package com.mrguo.dao.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.dto.sys.UserInfoApp;
import com.mrguo.entity.sys.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @param
 * @author 郭成兴
 * @return
 * @throws
 * @createdate 2019/4/14 12:05 PM
 * @updater 郭成兴
 * @updatedate 2019/4/14 12:05 PM
 */
@Repository("sysUserMapper")
public interface SysUserMapper extends MyMapper<SysUser> {
    /**
     * 自定义查询
     *
     * @param user
     * @return
     */
    List<SysUser> listCustom(Page page, @Param("user") SysUser user);

    @Select("select count(1) from sys_user where username = #{username}")
    int countByUserName(@Param("username") String username);

    @Select("select * from sys_user where username = #{username}")
    SysUser getDataByUserName(@Param("username") String username);

    List<SysUser> selectListDataByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取用户信息
     * 用户名，所属店
     * 用户数量，到期时间
     *
     * @return
     */
    UserInfoApp selectUserInfoByUserId(@Param("userId") Long userId);

    @Select("select value from sys_permission_data where user_id = #{userId}")
    String selectDataPermissionsByUserId(@Param("userId") Long userId);

    @Select("")
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}