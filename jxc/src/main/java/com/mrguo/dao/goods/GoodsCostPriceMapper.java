package com.mrguo.dao.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsCostPrice;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("goodsCostPriceMapper")
public interface GoodsCostPriceMapper extends MyMapper<GoodsCostPrice> {

    /**
     * 批量查询sku，成本价，仓库现有存货
     *
     * @param skus
     * @return
     */
    List<GoodsCostPrice> listCostPriceAndStockBySkus(List<String> skus);
}