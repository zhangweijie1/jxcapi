package com.mrguo.dao.bsd;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Specs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository("specsMapper")
public interface SpecsMapper extends MyMapper<Specs> {

    @Select("select count(1) from bsd_specs")
    int selectCount();

    /**
     * 查询这个名字是否存在
     *
     * @param name
     * @return
     */
    @Select("select count(1) from bsd_specs where name = #{name}")
    int selectCountByName(@Param("name") String name);

    List<Specs> listDataByNames(List<String> names);

    @Delete("delete from bsd_specs where p_id = #{pid}")
    int deleteChildrensByPid(@Param("pid") Long pid);
}