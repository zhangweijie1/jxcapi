package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysCard;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("sysCardMapper")
public interface SysCardMapper extends MyMapper<SysCard> {

    @Select("select * from sys_card where id = #{id}")
    SysCard selectById(String id);
}