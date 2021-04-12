package com.mrguo.entity.goods;

import com.mrguo.entity.log.LogGoodsStock;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/2210:54 PM
 * @updater 郭成兴
 * @updatedate 2020/6/2210:54 PM
 */
@Data
public class GoodsStockAndLog {

    /**
     * 库存List
     */
    List<GoodsStock> goodsStockList;

    /**
     * 库存日志List
     */
    List<LogGoodsStock> logGoodsStockList;
}
