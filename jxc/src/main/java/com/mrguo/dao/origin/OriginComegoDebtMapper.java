package com.mrguo.dao.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.origin.OriginComegoDebt;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("originComegoDebtMapper")
public interface OriginComegoDebtMapper extends MyMapper<OriginComegoDebt> {

    @Select("select * from origin_comego_debt where comego_id = #{comegoId}\n" +
            "order by create_time desc limit 1")
    OriginComegoDebt selectLastOriginData(@Param("comegoId") Long comegoId);
}