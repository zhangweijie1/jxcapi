package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.goods.GoodsLevelMapper;
import com.mrguo.entity.goods.GoodsLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsLevelServiceImpl {

    @Autowired
    private GoodsLevelMapper goodsLevelMapper;

    public void addListData(List<GoodsLevel> goodsLevels) throws CustomsException {
        if (goodsLevels.size() > 0) {
            if (goodsLevelMapper.insertList(goodsLevels) < 0) {
                throw new CustomsException("批量插入等级价失败！");
            }
        }
    }

    public List<GoodsLevel> selectBySkuIdsAndLevelId(List<String> skuIds, Long levelId) throws CustomsException {
        if (skuIds == null) {
            throw new CustomsException("商品ID不能为空！");
        }
        if (skuIds.size() == 0) {
            throw new CustomsException("商品ID不能为空！");
        }
        return goodsLevelMapper.selectBySkuIdsAndLevelId(skuIds, levelId);
    }

}
