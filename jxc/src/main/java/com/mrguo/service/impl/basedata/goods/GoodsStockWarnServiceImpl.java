package com.mrguo.service.impl.basedata.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsStockWarnMapper;
import com.mrguo.entity.goods.GoodsStockWarn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/1/410:41 PM
 * @updater 郭成兴
 * @updatedate 2020/1/410:41 PM
 */
@Service("goodsStockWarnServiceImpl")
public class GoodsStockWarnServiceImpl extends BaseServiceImpl<GoodsStockWarn> {

    @Autowired
    private GoodsStockWarnMapper goodsStockWarnMapper;

    @Override
    public MyMapper<GoodsStockWarn> getMapper() {
        return goodsStockWarnMapper;
    }

    public void addStockWarnListData(List<GoodsStockWarn> goodsStockWarnList) throws CustomsException {
        if (goodsStockWarnMapper.insertList(goodsStockWarnList) < 0) {
            throw new CustomsException("批量插入商品库存预警失败！");
        }
    }

    public HashMap<String, Object> countStockWarn() {
        int min = goodsStockWarnMapper.countStockWarnMin();
        int max = goodsStockWarnMapper.countStockWarnMax();
        HashMap<String, Object> map = new HashMap<>();
        map.put("minWarn", min);
        map.put("maxWarn", max);
        return map;
    }

    public IPage<Map<String, Object>> selectWarningGoods(PageParams<Map<String, Object>> pageParams) {
        Page<Map<String, Object>> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        List<Map<String, Object>> maps = goodsStockWarnMapper.selectWarningGoods(page, data);
        page.setRecords(maps);
        return page;
    }
}
