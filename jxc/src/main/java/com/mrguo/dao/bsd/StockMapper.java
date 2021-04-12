package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.vo.basedata.StockOfSkuVo;
import com.mrguo.entity.goods.GoodsStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: StockMapper
 * @Description: 库存Mapper
 * 1. 显示不同仓库的库存
 * 2. 合并不同仓库的库存
 * 3. 单个仓库的库存
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/12 11:02 上午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Repository("stockMapper")
public interface StockMapper extends MyMapper<GoodsStock> {

    /**
     * 分页查询库存状态Page(不同商品的列表)
     *
     * @param params
     * @author 郭成兴
     */
    List<StockOfSkuVo> selectStocksPage(Page<StockOfSkuVo> page,
                                        @Param("record") Map<String, Object> params);

    /**
     * 查询商品: 库存状态(显示不同仓库库存)
     *
     * @param skuId
     * @return
     */
    List<GoodsStock> selectStockGroupStoreBySkuId(@Param("skuId") Long skuId);

    /**
     * 查询多个商品: 库存状态（合并不同仓库的库存）
     *
     * @param skuIdList
     * @return
     */
    List<GoodsStock> selectStockMergeStoreBySkuIds(@Param("list") List<String> skuIdList);

    /**
     * 查询单个商品: 库存状态（合并不同仓库的）
     *
     * @param skuId
     * @return
     */
    GoodsStock getStockMergeStoreBySkuId(@Param("skuId") Long skuId);


    /**
     * 查询某个仓库多个sku的库存
     *
     * @param skuIds
     * @param storeId
     * @return
     */
    List<GoodsStock> selectStockBySkuIdsAndStoreId(@Param("list") List<String> skuIds,
                                                   @Param("storeId") Long storeId);
}