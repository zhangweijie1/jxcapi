package com.mrguo.service.impl.bill.uils;

import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.goods.GoodsCostPriceMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.goods.GoodsCostPrice;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.log.LogGoodsCostPrice;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsCostPriceServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.log.LogGoodsCostpriceServiceImpl;
import com.mrguo.service.impl.log.LogGoodsStockServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 专门处理成本价
 * @date 2020/6/223:45 PM
 * @updater 郭成兴
 * @updatedate 2020/6/223:45 PM
 */
@Service
public class BillHandleCostPriceServiceImpl {

    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private LogGoodsCostpriceServiceImpl logGoodsCostpriceService;
    @Autowired
    private LogGoodsStockServiceImpl logGoodsStockService;
    @Autowired
    private GoodsCostPriceServiceImpl goodsCostPriceService;
    @Autowired
    private GoodsCostPriceMapper goodsCostPriceMapper;

    /**
     * 只给单据记录下当前的costPrice
     * 无需重新计算
     * 如: 销售单等
     *
     * @param bill                 单据
     * @param remainGoodsStockList 库存信息
     * @param billDetailList       单据明细
     */
    public void setCurrentCostPriceLogs(Bill bill,
                                        List<GoodsStock> remainGoodsStockList,
                                        List<BillDetail> billDetailList) throws CustomsException {
        _setCurrentCostPriceLogs(
                bill.getId(),
                bill.getBillCat(),
                bill.getBillCatName(),
                bill.getBusinessTime(),
                remainGoodsStockList,
                billDetailList);
    }

    public void setCurrentCostPriceLogs(Bill bill,
                                        List<GoodsStock> remainGoodsStockList,
                                        List<BillDetail> billDetailList,
                                        String billCatName) throws CustomsException {
        _setCurrentCostPriceLogs(
                bill.getId(),
                bill.getBillCat(),
                billCatName,
                bill.getBusinessTime(),
                remainGoodsStockList,
                billDetailList);
    }

    /**
     * 计算如果进货单价有变化，则对成本会产生影响
     * 则重新计算
     *
     * @param bill
     * @param billDetailList
     */
    public void computedDefaultCostPrice(Bill bill,
                                         List<BillDetail> billDetailList) throws CustomsException {
        _computedDefaultCostPrice(
                bill.getId(),
                bill.getBillCat(),
                bill.getBillCatName(),
                bill.getBusinessTime(),
                billDetailList,
                null
        );
    }

    public void computedDefaultCostPrice(Bill bill,
                                         List<BillDetail> billDetailList,
                                         String billCatName) throws CustomsException {
        _computedDefaultCostPrice(
                bill.getId(),
                bill.getBillCat(),
                billCatName,
                bill.getBusinessTime(),
                billDetailList,
                null
        );
    }

    public void computedDefaultCostPrice(Bill bill,
                                         List<BillDetail> billDetailList,
                                         List<GoodsStock> remainGoodsStockList,
                                         String billCatName) throws CustomsException {
        _computedDefaultCostPrice(
                bill.getId(),
                bill.getBillCat(),
                billCatName,
                bill.getBusinessTime(),
                billDetailList,
                remainGoodsStockList
        );
    }


    /**
     * 重新计算所有单据商品的成本:
     * 如: 取消进货，那么为了维持成本的准确性，该进货单之后的所有单据成本都需要重新计算
     */
    public void computedAllCostPriceAfterBill(Bill bill) throws CustomsException {
        Date businessTime = bill.getBusinessTime();
        List<BillDetail> detailList = billDetailService.listDataByBillId(bill.getId());
        List<Long> skuIds = detailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        // 直到取消前期末的成本,库存
        List<LogGoodsCostPrice> lastCostPriceList = logGoodsCostpriceService.getLastDataByBusinessTime(businessTime);
        if (lastCostPriceList.size() == 0) {
            // 说明该单据是最后一个
            // 则不需要为了取消单据，做出成本重置
            return;
        }
        List<LogGoodsStock> lastStockList = logGoodsStockService.getLastDataByBusinessTimeGroupSku(businessTime);
        // cancle单据之后的单据成本
        List<LogGoodsCostPrice> goodsCostPriceList = logGoodsCostpriceService.getDataAfterBusinessTimeBySkuIds(businessTime, skuIds);
        for (Long skuId : skuIds) {
            // sku的成本日志
            List<LogGoodsCostPrice> afterCostPrice = goodsCostPriceList.stream().filter(item -> {
                return skuId.equals(item.getSkuId());
            }).collect(Collectors.toList());
            // sku的上期末成本价
            List<LogGoodsCostPrice> lastCostPrices = lastCostPriceList.stream().filter(item -> {
                return skuId.equals(item.getSkuId());
            }).collect(Collectors.toList());
            String errorMessage = "一个SKU的上期期末成本只能是一个";
            if (lastCostPrices.size() != 1) {
                throw new BusinessException(errorMessage);
            }
            LogGoodsCostPrice lastCostPrice = lastCostPrices.get(0);
            // skud的上期末库存
            List<LogGoodsStock> lastStocks = lastStockList.stream().filter(item -> {
                return skuId.equals(item.getSkuId());
            }).collect(Collectors.toList());
            if (lastStocks.size() != 1) {
                throw new BusinessException(errorMessage);
            }
            LogGoodsStock lastStock = lastStocks.get(0);
            handleOneSku(lastStock, lastCostPrice, afterCostPrice);
        }
    }

    /**
     * 处理一个sku的成本
     *
     * @param
     */
    private void handleOneSku(LogGoodsStock lastStock,
                              LogGoodsCostPrice lastCostPrice,
                              List<LogGoodsCostPrice> goodsCostPriceList) {
        BigDecimal originStock = new BigDecimal("0");
        BigDecimal currentPrice = lastCostPrice.getRemainPriceCost();
        BigDecimal currentStock = originStock.add(lastStock.getRemQtyIn())
                .subtract(lastStock.getRemQtyOut());
        for (LogGoodsCostPrice goodsCostPrice : goodsCostPriceList) {
            if ("purchase".equals(goodsCostPrice.getBillCatName())) {
                // 如果是进货单，重新计算成本
                goodsCostPrice.setPriceCost(currentPrice);
                LogGoodsCostPrice logGoodsCostPrice = computedCostPrice(currentPrice, currentStock, goodsCostPrice);
                currentPrice = logGoodsCostPrice.getRemainPriceCost();
            } else {
                // 如果非进货单，直接设置当前成本
                goodsCostPrice.setPriceCost(currentPrice);
            }
        }
        System.out.println("最终成本：" + currentPrice);
    }

    /**
     * 计算成本价
     * 此前成本，此前库存，当次成本，当次数量
     *
     * @return
     */
    private LogGoodsCostPrice computedCostPrice(BigDecimal currentCostPrice,
                                                BigDecimal currentStock,
                                                LogGoodsCostPrice logGoodsCostPrice) {
        BigDecimal totalOldPrice = currentCostPrice.multiply(currentStock);
        //
        BigDecimal priceCost = logGoodsCostPrice.getPriceCost();
        BigDecimal quantity = logGoodsCostPrice.getQuantity();
        BigDecimal thisTimetotalPrice = priceCost.multiply(quantity);
        //
        BigDecimal totalPrice = totalOldPrice.add(thisTimetotalPrice);
        BigDecimal totalQty = currentStock.add(quantity);
        BigDecimal remainPrice = totalPrice.divide(totalQty, 4);
        logGoodsCostPrice.setRemainPriceCost(remainPrice);
        return logGoodsCostPrice;
    }

    private void _setCurrentCostPriceLogs(Long billId,
                                          String billCat,
                                          String billCatName,
                                          Date businessTime,
                                          List<GoodsStock> remainGoodsStockList,
                                          List<BillDetail> billDetailList) throws CustomsException {
        Date date = new Date();
        // 获取原有成本
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsCostPrice> oldCostPriceList = goodsCostPriceService.listCostPriceAndStockBySkus(skuIds);
        //
        ArrayList<LogGoodsCostPrice> logGoodsCostPriceArrayList = new ArrayList<>();
        for (Long skuId : skuIds) {
            LogGoodsCostPrice logGoodsCostPrice = new LogGoodsCostPrice();
            // 构建一般字段
            logGoodsCostPrice.setSkuId(skuId);
            logGoodsCostPrice.setBillId(billId);
            logGoodsCostPrice.setBusinessTime(businessTime);
            logGoodsCostPrice.setCreateTime(date);
            logGoodsCostPrice.setBillCat(billCat);
            logGoodsCostPrice.setBillCatName(billCatName);
            // 构建数量
            BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
            BigDecimal qtyInBaseUnit = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(skuId, billDetailList);
            List<GoodsStock> goodsStocks = remainGoodsStockList.stream().filter(item -> {
                return item.getSkuId().equals(skuId);
            }).collect(Collectors.toList());
            if (goodsStocks.size() != 1) {
                throw new BusinessException("有商品没有库存数据");
            }
            GoodsStock goodsStock = goodsStocks.get(0);
            BigDecimal remainQty = goodsStock.getOriginQuantity().add(goodsStock.getQuantityIn())
                    .add(goodsStock.getWaitQuantityIn())
                    .subtract(goodsStock.getQuantityOut())
                    .subtract(goodsStock.getWaitQuantityOut());
            logGoodsCostPrice.setQuantity(qtyInBaseUnit);
            logGoodsCostPrice.setRemainQty(remainQty);
            // 构建成本价
            List<GoodsCostPrice> collect = oldCostPriceList.stream().filter(item -> {
                return item.getSkuId().equals(skuId);
            }).collect(Collectors.toList());
            if (collect.size() != 1) {
                throw new BusinessException("有商品没有成本数据");
            }
            GoodsCostPrice goodsCostPrice = collect.get(0);
            BigDecimal targetPriceCost = goodsCostPrice.getPriceCost();
            logGoodsCostPrice.setPriceCost(targetPriceCost);
            logGoodsCostPrice.setRemainPriceCost(targetPriceCost);
            logGoodsCostPriceArrayList.add(logGoodsCostPrice);
        }
        logGoodsCostpriceService.insertListData(logGoodsCostPriceArrayList);
    }

    private void _computedDefaultCostPrice(Long billId,
                                           String billCat,
                                           String billCatName,
                                           Date businessTime,
                                           List<BillDetail> billDetailList,
                                           List<GoodsStock> remainGoodsStockList) throws CustomsException {
        // 原有的成本
        List<String> skuIds = billDetailList.stream().map(item -> {
            return String.valueOf(item.getSkuId());
        }).distinct().collect(Collectors.toList());
        List<GoodsCostPrice> oldCostAndStokPriceList = goodsCostPriceMapper.listCostPriceAndStockBySkus(skuIds);
        // 结余成本
        List<GoodsCostPrice> oldCostPriceListParams = oldCostAndStokPriceList.stream().map(item -> {
            GoodsCostPrice goodsCostPrice = new GoodsCostPrice();
            BeanUtils.copyProperties(item, goodsCostPrice);
            return goodsCostPrice;
        }).collect(Collectors.toList());
        List<GoodsCostPrice> remainGoodsCostPrices = goodsCostPriceService.batchUpdateCostPriceByskus(
                oldCostPriceListParams, billDetailList);
        // 成本日志
        if (remainGoodsStockList == null) {
            logGoodsCostpriceService.addListDataBySkus(
                    billId,
                    billCat,
                    billCatName,
                    businessTime,
                    billDetailList,
                    oldCostAndStokPriceList,
                    remainGoodsCostPrices);
        } else {
            logGoodsCostpriceService.addListDataBySkus(
                    billId,
                    billCat,
                    billCatName,
                    businessTime,
                    billDetailList,
                    oldCostAndStokPriceList,
                    remainGoodsStockList,
                    remainGoodsCostPrices);
        }
    }
}
