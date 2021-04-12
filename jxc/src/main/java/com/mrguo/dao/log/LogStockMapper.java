package com.mrguo.dao.log;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.log.LogGoodsCostPrice;
import com.mrguo.entity.log.LogGoodsStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("logStockMapper")
public interface LogStockMapper extends MyMapper<LogGoodsStock> {

    /**
     * 查询某商品的库存日志
     *
     * @param params
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/1/4 10:45 PM
     * @updater 郭成兴
     * @updatedate 2020/1/4 10:45 PM
     */
    List<LogGoodsStock> selectStockLogBySkuId(Page page, @Param("record") Map<String, Object> params);

    /**
     * 批量新增日志
     */
    int batchInsertLogs(List<LogGoodsStock> logGoodsStocks);

    List<LogGoodsStock> getLastDataByBusinessTimeGroupSku(@Param("date") Date date);
}