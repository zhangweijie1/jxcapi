package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodspriceMapper;
import com.mrguo.entity.goods.GoodsPrice;
import com.mrguo.entity.goods.GoodsSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 商品价格
 * @date 2020/1/82:22 PM
 * @updater 郭成兴
 * @updatedate 2020/1/82:22 PM
 */
@Service("goodspriceServiceImpl")
public class GoodsPriceServiceImpl extends BaseServiceImpl<GoodsPrice> {

    @Autowired
    private GoodspriceMapper goodspriceMapper;

    @Override
    public MyMapper<GoodsPrice> getMapper() {
        return goodspriceMapper;
    }

    @Override
    public GoodsPrice saveData(GoodsPrice entity) {
        return super.saveData(entity);
    }

    /**
     * 新增多个sku的价格数据
     *
     * @param goodsPriceList
     * @return
     */
    public void addPriceListData(List<GoodsPrice> goodsPriceList) throws CustomsException {
        if (goodspriceMapper.insertList(goodsPriceList) < 0) {
            throw new CustomsException("批量插入商品价格失败！");
        }
    }

    /**
     * 新增一个Sku规格的单位price
     *
     * @param goodsPrices
     */
    private void addSingleSpecsPrice(List<GoodsPrice> goodsPrices, GoodsSku goodssku) {
        for (GoodsPrice goodsprice : goodsPrices) {
            goodsprice.setSkuId(goodssku.getId());
            saveData(goodsprice);
        }
    }

    public void delDataBySkuId(Long skuId) throws CustomsException {
        if (goodspriceMapper.delDataBySkuId(skuId) < 0) {
            throw new CustomsException("删除商品价格失败！");
        }
    }

}
