package com.mrguo.dao.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.origin.OriginSkuCostprice;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository("originSkuCostpriceMapper")
public interface OriginSkuCostpriceMapper extends MyMapper<OriginSkuCostprice> {

}