package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.CustomerCat;
import com.mrguo.entity.sys.SysBillNoRule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customerCatMapper")
public interface CustomerCatMapper extends MyMapper<CustomerCat> {

    /**
     * 根据父节点查找类别
     * @param parentId
     * @return
     */
    @Select("select * from bsd_customer_cat where p_id=#{parentId}")
    List<CustomerCat> findByParentId(String parentId);

    /**
     * 根据父id获取son的个数
     *
     * @param pid
     * @return
     */
    @Select("select count(1) from bsd_customer_cat where p_id = #{pid}")
    int countChildrensByPid(@Param("pid") String pid);

    @Select("select max(id) from bsd_customer_cat where p_id = #{pid}")
    String getMaxIdByPid(@Param("pid") String pid);

    @Select("select count(1) from bsd_customer_cat where name = #{name}")
    int countByName(@Param("name") String name);
}