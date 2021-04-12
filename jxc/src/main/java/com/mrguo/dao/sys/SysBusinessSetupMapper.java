package com.mrguo.dao.sys;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Store;
import com.mrguo.entity.sys.SysBusinessSetup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository("sysBusinessSetupMapper")
public interface SysBusinessSetupMapper extends MyMapper<SysBusinessSetup> {

    /**
     * 设置值
     *
     * @param value
     * @return
     */
    @Update("UPDATE sys_business_setup SET value = #{value}")
    int updateValue(@Param("value") String value);

    @Select("select value from sys_business_setup")
    String selectValue();

    @Select("select * from sys_business_setup")
    SysBusinessSetup selectSetup();
}