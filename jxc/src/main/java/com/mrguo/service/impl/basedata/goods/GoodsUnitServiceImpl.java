package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsUnitMapper;
import com.mrguo.entity.goods.GoodsUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/1/82:22 PM
 * @updater 郭成兴
 * @updatedate 2020/1/82:22 PM
 */
@Service("goodsUnitServiceImpl")
public class GoodsUnitServiceImpl extends BaseServiceImpl<GoodsUnit> {

    @Autowired
    private GoodsUnitMapper goodsUnitMapper;

    @Override
    public MyMapper<GoodsUnit> getMapper() {
        return goodsUnitMapper;
    }

    /**
     * 新增多个unit的数据
     *
     * @param goodsUnitList
     * @return
     */
    public void addListData(List<GoodsUnit> goodsUnitList) throws CustomsException {
        if (goodsUnitMapper.insertList(goodsUnitList) < 0) {
            throw new CustomsException("批量插入商品单位失败！");
        }
    }

    /**
     * 获取单位by skuid
     *
     * @param skuId
     * @return
     */
    public List<GoodsUnit> getUnitListBySkuId(Long skuId) {
        return goodsUnitMapper.getListBySkuId(skuId);
    }

    public int countByUnitId(Long unitId) {
        return goodsUnitMapper.countByUnitId(unitId);
    }

    public List<GoodsUnit> listAllData() {
        return goodsUnitMapper.listAllData();
    }

    public void delDataBySkuId(Long skuId) throws CustomsException {
        if (goodsUnitMapper.delDataBySkuId(skuId) == 0) {
            throw new CustomsException("删除商品单位失败！");
        }
    }

    public void delDataBySkuId(List<Long> skuIds) {
        List<String> collect = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        goodsUnitMapper.delDataBySkuIds(collect);
    }

    public void delDataBySkuIdUnitId(Long skuId, Long unitId) throws CustomsException {
        if (goodsUnitMapper.delDataBySkuIdUnitId(skuId, unitId) == 0) {
            throw new CustomsException("删除商品单位失败！");
        }
    }
}
