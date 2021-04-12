package com.mrguo.dao.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.log.LogGoodsCostPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName LogGoodsCostPriceMapper
 * @Description 商品成本明细查询
 * @date 2020/3/25 4:13 PM
 * @updater 郭成兴
 * @updatedate 2020/3/25 4:13 PM
 */
@Repository("logGoodsCostPriceMapper")
public interface LogGoodsCostPriceMapper extends MyMapper<LogGoodsCostPrice> {

    /**
     * 查下某商品的成本价明细
     *
     * @param data
     * @param page
     * @return List<LogGoodsCostPrice>
     */
    List<LogGoodsCostPrice> listDataBySkuId(Page<LogGoodsCostPrice> page, @Param("record") Map<String, Object> data);

    List<LogGoodsCostPrice> getLastDataByBusinessTime(@Param("date") Date date);

    List<LogGoodsCostPrice> getDataAfterBusinessTimeBySkuIds(@Param("date") Date date,
                                                             @Param("list") List<String> skuIds);
}