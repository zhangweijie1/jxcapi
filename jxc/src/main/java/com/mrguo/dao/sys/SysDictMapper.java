package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sysDictMapper")
public interface SysDictMapper extends MyMapper<SysDict> {

    List<SysDict> selectByParent(@Param("parent") String parent);

}