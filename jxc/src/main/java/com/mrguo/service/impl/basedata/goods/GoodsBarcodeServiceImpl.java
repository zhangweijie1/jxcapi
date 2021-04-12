package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsBarcodeMapper;
import com.mrguo.entity.goods.GoodsBarcode;
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
@Service("goodBarcodeServiceImpl")
public class GoodsBarcodeServiceImpl extends BaseServiceImpl<GoodsBarcode> {

    @Autowired
    private GoodsBarcodeMapper goodsBarcodeMapper;

    @Override
    public MyMapper<GoodsBarcode> getMapper() {
        return goodsBarcodeMapper;
    }


    /**
     * 新增多个sku的价格数据
     *
     * @param goodsBarcodeList
     * @return
     */
    public void addBarcodeListData(List<GoodsBarcode> goodsBarcodeList) throws CustomsException {
        if (goodsBarcodeMapper.insertList(goodsBarcodeList) < 0) {
            throw new CustomsException("批量插入条形码失败！");
        }
    }

    public void delDataBySkuId(Long skuId) throws CustomsException {
        if (goodsBarcodeMapper.delDataBySkuId(skuId) < 0) {
            throw new CustomsException("删除条形码失败！");
        }
    }

    public void delDataBySkuId(List<Long> skuIds) {
        List<String> collect = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        goodsBarcodeMapper.delDataBySkuIds(collect);
    }

    public List<String> getBarcodeListByCodes(List<String> codes) {
        return goodsBarcodeMapper.selectBarcodeListByCodes(codes);
    }

    public List<String> getBarcodeListBySkuId(Long skuId) {
        return goodsBarcodeMapper.selectBarcodeListBySkuId(skuId);
    }

    public List<GoodsBarcode> listAllData() {
        return goodsBarcodeMapper.selectAll();
    }
}
