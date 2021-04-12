package com.mrguo.dao.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("finComegoReceDebtMapper")
public interface FinComegoReceDebtMapper {

    /**
     * 查询应收欠款按照客户id分组
     *
     * @param page
     * @param data
     * @return
     */
    List<Map<String, Object>> listDataGroupByComegoPage(Page<Map<String, Object>> page,
                                                      @Param("record") Map<String, Object> data);

    /**
     * 查询客户的欠款明细
     *
     * @param page
     * @param data
     * @return
     */
    List<Map<String, Object>> listDebtDetailByComegoId(Page<Map<String, Object>> page,
                                                       @Param("record") Map<String, Object> data);

    /**
     * 所有客户的欠款统计
     *
     * @param data
     * @return
     */
    Map<String, Object> selectStatistics(@Param("record") Map<String, Object> data);
}