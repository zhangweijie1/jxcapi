package com.mrguo.dao.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.fin.FinCapitalCat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("finPayCatMapper")
public interface FinCapitalCatMapper extends MyMapper<FinCapitalCat> {

    @Select("select id as value, name as label from fin_capital_cat where direction = '0'")
    List<FinCapitalCat> getPayOptions();

    @Select("select id as value, name as label from fin_capital_cat where direction = '1'")
    List<FinCapitalCat> getReceOptions();

    /**
     * 自定义查询
     *
     * @return
     */
    List<FinCapitalCat> listCustom(Page page, @Param("record") Map params);
}