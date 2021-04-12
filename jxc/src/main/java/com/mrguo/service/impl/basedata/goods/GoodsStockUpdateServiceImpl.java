package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.bill.uils.BillUtilsGenerator;
import com.mrguo.service.impl.bill.uils.BillSkuQtyComputer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/1/410:41 PM
 * @updater 郭成兴
 * @updatedate 2020/1/410:41 PM
 */
@Service
public class GoodsStockUpdateServiceImpl {

    @Autowired
    private GoodsStockServiceImpl goodsStockService;

    /**
     * 修改stock待入库
     *
     * @param storeId
     * @param detailList
     * @param inout      进还是出
     * @param addsub     加还是减
     */
    public void updateStockWaite(Long storeId,
                                 List<BillDetail> detailList,
                                 String inout,
                                 String addsub) {

        List<Long> skuIds = detailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        HashSet<Long> h = new HashSet<>(skuIds);
        skuIds.clear();
        skuIds.addAll(h);
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, storeId);
        if (skuIds.size() != goodsStockList.size()) {
            throw new BusinessException("有商品没有库存信息！");
        }
        for (GoodsStock goodsStock : goodsStockList) {
            BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
            BigDecimal qtyInBaseUnit = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            if ("in".equals(inout)) {
                if ("add".equals(addsub)) {
                    goodsStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn().add(qtyInBaseUnit));
                } else {
                    goodsStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn().subtract(qtyInBaseUnit));
                }
            }
            if ("out".equals(inout)) {
                if ("add".equals(addsub)) {
                    goodsStock.setWaitQuantityOut(goodsStock.getWaitQuantityOut().add(qtyInBaseUnit));
                } else {
                    goodsStock.setWaitQuantityOut(goodsStock.getWaitQuantityOut().subtract(qtyInBaseUnit));
                }
            }
        }
        goodsStockService.updateListSelectiveData(goodsStockList);
    }

    /**
     * 修改stock入库量，和待入库量
     * @param storeId
     * @param detailList
     * @param inout
     * @param addsub（以入库量为准）
     * @throws CustomsException
     */
    public void updateStockAndWaite(Long storeId,
                                 List<BillDetail> detailList,
                                 String inout,
                                 String addsub) throws CustomsException {

        List<Long> skuIds = detailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        HashSet h = new HashSet<>(skuIds);
        skuIds.clear();
        skuIds.addAll(h);
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, storeId);
        if (skuIds.size() != goodsStockList.size()) {
            throw new BusinessException("有商品没有库存信息！");
        }
        for (GoodsStock goodsStock : goodsStockList) {
            BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
            BigDecimal qtyInBaseUnit = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            if ("in".equals(inout)) {
                if ("add".equals(addsub)) {
                    goodsStock.setQuantityIn(goodsStock.getQuantityIn().add(qtyInBaseUnit));
                    goodsStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn().subtract(qtyInBaseUnit));
                } else {
                    goodsStock.setQuantityIn(goodsStock.getQuantityIn().subtract(qtyInBaseUnit));
                    goodsStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn().add(qtyInBaseUnit));
                }
            }
            if ("out".equals(inout)) {
                if ("add".equals(addsub)) {
                    goodsStock.setQuantityOut(goodsStock.getQuantityOut().add(qtyInBaseUnit));
                    goodsStock.setWaitQuantityOut(goodsStock.getWaitQuantityOut().subtract(qtyInBaseUnit));
                } else {
                    goodsStock.setQuantityOut(goodsStock.getQuantityOut().subtract(qtyInBaseUnit));
                    goodsStock.setWaitQuantityOut(goodsStock.getWaitQuantityOut().add(qtyInBaseUnit));
                }
            }
        }
        goodsStockService.updateListSelectiveData(goodsStockList);
    }
}
