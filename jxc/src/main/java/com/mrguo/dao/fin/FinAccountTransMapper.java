package com.mrguo.dao.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.fin.FinAccountTrans;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("finAccountTransMapper")
public interface FinAccountTransMapper extends MyMapper<FinAccountTrans> {

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<FinAccountTrans> listCustom(Page page, @Param("record") Map params);

    FinAccountTrans selectOneById(@Param("id") Long id);
}