package com.mrguo.dao.sys;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysEmployee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("sysEmployeeMapper")
public interface SysEmployeeMapper extends MyMapper<SysEmployee> {

    /**
     * 自定义查询
     *
     * @param sysEmployee
     * @return
     */
    List<SysEmployee> listPage(Page<SysEmployee> page,
                                 @Param("types") List<String> types,
                                 @Param("record") SysEmployee sysEmployee);

    /**
     * byId查询
     *
     * @param id
     * @return
     */
    SysEmployee getDataById(@Param("id") Long id);

    List<String> getRolesByUserId(@Param("userId") Long userId);

    /**
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2019/12/27 11:06 AM
     * @updater 郭成兴
     * @updatedate 2019/12/27 11:06 AM
     */
    List<SysEmployee> listOptions(@Param("record") Map map);

    @Select("select * from sys_employee where id = #{userId}")
    SysEmployee selectByUserId(@Param("userId") Long userId);

    @Select("select * from sys_employee where type = '2'")
    SysEmployee selectManager();
}