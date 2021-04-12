package com.mrguo.dao.sys;


import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysBillNoRule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sysBillNoRuleMapper")
public interface SysBillNoRuleMapper extends MyMapper<SysBillNoRule> {

    /**
     * 根据billCat查询数据
     *
     * @param billCat
     * @return
     */
    @Select("select * from t_bill_no_rule where bill_cat = #{billCat}")
    List<SysBillNoRule> listDataByBillCat(@Param("billCat") String billCat);

    @Select("select * from t_bill_no_rule")
    List<SysBillNoRule> selectAllPage(Page page);

}