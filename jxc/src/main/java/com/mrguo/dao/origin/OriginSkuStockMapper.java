package com.mrguo.dao.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.origin.OriginSkuStock;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository("originSkuStockMapper")
public interface OriginSkuStockMapper extends MyMapper<OriginSkuStock> {

}