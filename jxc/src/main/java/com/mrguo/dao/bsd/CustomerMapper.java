package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.service.impl.excle.ExcleCustomerData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName CustomerMapper
 * @Description 客户Repository接口
 * @date 2019/8/5 6:03 PM
 * @updater 郭成兴
 * @updatedate 2019/8/5 6:03 PM
 */
@Repository("customerMapper")
public interface CustomerMapper extends MyMapper<Customer> {

    @Update("update bsd_customer set is_default='0'")
    int cancleDefault();

    @Select(value = "select * from bsd_customer where name like #{name}")
    List<Customer> findByName(String name);

    Customer findById(Long id);

    List<Customer> listPage(Page<Customer> page, @Param("record") Map<String, Object> data);

    /**
     * 查询下拉框数据 by关键字
     * @param keywords
     * @return
     */
    List<Customer> listOptions(String keywords);

    @Select("select count(1) from bsd_customer where code = #{code}")
    int countByCode(@Param("code") String code);

    @Select("select count(1) from bsd_customer where cat_id = #{catId}")
    int countByCatId(@Param("catId") String catId);

    @Select("select count(1) from bsd_customer where level_id = #{levelId}")
    int countByLevelId(@Param("levelId") Long levelId);

    List<ExcleCustomerData> exportData();
}