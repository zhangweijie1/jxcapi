package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsCostPriceMapper;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.goods.GoodsCostPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
@Service("goodsCostPriceServiceImpl")
public class GoodsCostPriceServiceImpl extends BaseServiceImpl<GoodsCostPrice> {

    @Autowired
    private GoodsCostPriceMapper goodsCostPriceMapper;

    @Value("${amount.digit}")
    private int amountDigit;

    @Override
    public MyMapper<GoodsCostPrice> getMapper() {
        return goodsCostPriceMapper;
    }

    /**
     * 批量新增sku成本
     *
     * @param goodsCostPriceList
     * @throws CustomsException
     */
    void addCostPriceListData(List<GoodsCostPrice> goodsCostPriceList) throws CustomsException {
        if (goodsCostPriceMapper.insertList(goodsCostPriceList) < 0) {
            throw new CustomsException("批量插入商品成本记录失败！");
        }
    }

    /**
     * 更新商品成本价(进货单需要)
     * (此次的数量 * 此次成本价)+(原有数量 * 原有成本价)/ (此次数量+原有数量)
     */
    public List<GoodsCostPrice> batchUpdateCostPriceByskus(List<GoodsCostPrice> oldCostPriceList,
                                                           List<BillDetail> thisTimeDetailList) throws CustomsException {

        for (GoodsCostPrice oldGoodsCostPrice : oldCostPriceList) {
            BigDecimal oldTotalPrice = oldGoodsCostPrice.getPriceCost().multiply(oldGoodsCostPrice.getQty());
            BillDetail thisTime = getCostPriceInfoBySkuId(oldGoodsCostPrice.getSkuId(), thisTimeDetailList);
            BigDecimal thisTimeTotalCostPrice = thisTime.getTotalCostPrice();
            BigDecimal thisTimeQtyInBaseUnit = thisTime.getQtyInBaseUnit();
            // 总计
            BigDecimal totalPrice = oldTotalPrice.add(thisTimeTotalCostPrice);
            BigDecimal totalQty = oldGoodsCostPrice.getQty().add(thisTimeQtyInBaseUnit);
            BigDecimal newPrice = totalPrice.divide(totalQty, amountDigit);
            oldGoodsCostPrice.setPriceCost(newPrice);
            oldGoodsCostPrice.setBillTotalPriceCost(thisTimeTotalCostPrice);
            oldGoodsCostPrice.setBillTotalQtyInBaseUnit(thisTimeQtyInBaseUnit);
        }
        if (goodsCostPriceMapper.batchUpdateByPrimaryKeySelective(oldCostPriceList) < 1) {
            throw new CustomsException("更新成本价失败！");
        }
        return oldCostPriceList;
    }

    /**
     * 获取商品成本信息
     *
     * @param skuId
     * @param thisTimeCostPrice
     * @return
     */
    public BillDetail getCostPriceInfoBySkuId(Long skuId, List<BillDetail> thisTimeCostPrice) {
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        for (BillDetail b : thisTimeCostPrice) {
            if (b.getSkuId().equals(skuId)) {
                totalQty = totalQty.add(b.getQuantity().multiply(b.getUnitMulti()));
                totalCostPrice = totalCostPrice.add(b.getQuantity().multiply(b.getPrice()));
            }
        }
        BillDetail billDetail = new BillDetail();
        billDetail.setQtyInBaseUnit(totalQty);
        billDetail.setTotalCostPrice(totalCostPrice);
        return billDetail;
    }

    public List<GoodsCostPrice> listCostPriceAndStockBySkus(List<Long> skuIds) {
        List<String> collect = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        return goodsCostPriceMapper.listCostPriceAndStockBySkus(collect);
    }
}
