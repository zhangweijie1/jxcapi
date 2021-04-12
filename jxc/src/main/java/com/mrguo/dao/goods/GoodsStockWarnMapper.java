package com.mrguo.dao.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsStockWarn;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("goodsStockWarnMapper")
public interface GoodsStockWarnMapper extends MyMapper<GoodsStockWarn> {

    int countStockWarnMax();

    int countStockWarnMin();

    List<Map<String, Object>> selectWarningGoods(IPage<Map<String, Object>> page,
                                                 @Param("record") Map<String, Object> data);
}