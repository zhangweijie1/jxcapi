package com.mrguo.service.impl.bill.uils;

import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bill.BillStock;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.entity.log.LogGoodsStock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: BillSkuQtyComputer
 * @Description: 库存相关的计算
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 1:38 下午
 * @Copyright 南通市韶光科技有限公司
 **/
public class BillSkuQtyComputer {

    /**
     * 获取单据明细里，某skuId基于基础单位的数量
     *
     * @return
     */
    public BigDecimal getQtyOfBaseUnitInDetailsBySkuId(Long skuId,
                                                       List<BillDetail> detailList) {
        BigDecimal result = BigDecimal.ZERO;
        for (BillDetail billDetail : detailList) {
            if (billDetail.getSkuId().equals(skuId)) {
                BigDecimal target = billDetail.getQuantity().multiply(billDetail.getUnitMulti());
                result = result.add(target);
            }
        }
        return result;
    }

    public BigDecimal getQtyOfBaseUnitInDetails(List<BillDetail> detailList) {
        BigDecimal result = BigDecimal.ZERO;
        for (BillDetail billDetail : detailList) {
            BigDecimal target = billDetail.getQuantity().multiply(billDetail.getUnitMulti());
            result = result.add(target);
        }
        return result;
    }

    /**
     * 获取在途库存和库存Log
     * 默认方法 stockList 和 logStocks
     * 新增在途库存时.
     *
     * @param bill           单据
     * @param detailList     单据明细
     * @param goodsStockList 商品库存
     * @return
     */
    public GoodsStockAndLog getDefaultAddWaiteQty(Bill bill,
                                                  List<BillDetail> detailList,
                                                  List<GoodsStock> goodsStockList
    ) {
        Date date = new Date();
        String direction = bill.getDirection();
        List<LogGoodsStock> logGoodsStockList = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {
            BigDecimal thisTimeQtyInBaseUnit = getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            // 1. 处理库存日志 Log
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setCreateTime(date);
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setDirection(direction);
            logGoodsStock.setQuantity(thisTimeQtyInBaseUnit);
            setLogGoodsStockByBill(logGoodsStock, bill);
            // 结余库存
            BigDecimal remainWaiteQtyIn = goodsStock.getWaitQuantityIn().add(thisTimeQtyInBaseUnit);
            logGoodsStock.setRemWaiteQtyIn(remainWaiteQtyIn);
            logGoodsStockList.add(logGoodsStock);
            // 库存 goodsStock
            goodsStock.setWaitQuantityIn(remainWaiteQtyIn);
        }
        GoodsStockAndLog stockAndLog = new GoodsStockAndLog();
        stockAndLog.setLogGoodsStockList(logGoodsStockList);
        stockAndLog.setGoodsStockList(goodsStockList);
        return stockAndLog;
    }

    public GoodsStockAndLog getDefaultSubWaiteQty(Bill bill,
                                                  List<BillDetail> detailList,
                                                  List<GoodsStock> goodsStockList
    ) {
        Date date = new Date();
        String direction = bill.getDirection();
        List<LogGoodsStock> logGoodsStockList = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {
            BigDecimal thisTimeQtyInBaseUnit = getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            // 1. 处理库存日志 Log
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setCreateTime(date);
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setDirection(direction);
            logGoodsStock.setQuantity(thisTimeQtyInBaseUnit);
            setLogGoodsStockByBill(logGoodsStock, bill);
            // 结余库存
            BigDecimal remainWaiteQtyOut = goodsStock.getWaitQuantityOut().add(thisTimeQtyInBaseUnit);
            logGoodsStock.setRemWaiteQtyOut(remainWaiteQtyOut);
            logGoodsStockList.add(logGoodsStock);
            // 库存 goodsStock
            goodsStock.setWaitQuantityOut(remainWaiteQtyOut);
        }
        GoodsStockAndLog stockAndLog = new GoodsStockAndLog();
        stockAndLog.setLogGoodsStockList(logGoodsStockList);
        stockAndLog.setGoodsStockList(goodsStockList);
        return stockAndLog;
    }

    /**
     * 默认情况下，获取存货(现存)库存
     * 新增入库量
     * qty ++ ; waite_qty --
     *
     * @param bill           单据，如：进货，销售等
     * @param detailList     单据的明细
     * @param goodsStockList 单据的库存信息
     * @return
     */
    public GoodsStockAndLog getDefaultAddExistQty(BillStock bill,
                                                  List<BillDetail> detailList,
                                                  List<GoodsStock> goodsStockList) {
        Date date = new Date();
        List<LogGoodsStock> logGoodsStockList = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {
            // 1. 本次的数量, 本次后剩余入库量， 本次后剩余出库量
            BigDecimal thisTimeQtyInBaseUnit = getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            BigDecimal remainQtyIn = goodsStock.getQuantityIn().add(thisTimeQtyInBaseUnit);
            BigDecimal remainWaiteQtyIn = goodsStock.getWaitQuantityIn().subtract(thisTimeQtyInBaseUnit);
            // 2. 库存Logs
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setCreateTime(date);
            logGoodsStock.setQuantity(thisTimeQtyInBaseUnit);
            setLogGoodsStockByBill(logGoodsStock, bill);
            //
            logGoodsStock.setRemQtyIn(remainQtyIn);
            logGoodsStock.setRemWaiteQtyIn(remainWaiteQtyIn);
            logGoodsStockList.add(logGoodsStock);
            // 3. 商品库存 GoodsStock
            goodsStock.setQuantityIn(remainQtyIn);
            goodsStock.setWaitQuantityIn(remainWaiteQtyIn);
        }
        // 整合返回
        GoodsStockAndLog goodsStockDto = new GoodsStockAndLog();
        goodsStockDto.setLogGoodsStockList(logGoodsStockList);
        goodsStockDto.setGoodsStockList(goodsStockList);
        return goodsStockDto;
    }

    public GoodsStockAndLog getDefaultSubExistQty(BillStock bill,
                                                  List<BillDetail> detailList,
                                                  List<GoodsStock> goodsStockList) {
        Date date = new Date();
        List<LogGoodsStock> logGoodsStockList = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {
            // 1. 本次的数量, 本次后剩余入库量， 本次后剩余出库量
            BigDecimal thisTimeQtyInBaseUnit = getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), detailList);
            BigDecimal remainQtyOut = goodsStock.getQuantityOut().add(thisTimeQtyInBaseUnit);
            BigDecimal remainWaiteQtyOut = goodsStock.getWaitQuantityOut().subtract(thisTimeQtyInBaseUnit);
            // 2. 库存Logs
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setCreateTime(date);
            logGoodsStock.setQuantity(thisTimeQtyInBaseUnit);
            setLogGoodsStockByBill(logGoodsStock, bill);
            //
            logGoodsStock.setRemQtyOut(remainQtyOut);
            logGoodsStock.setRemWaiteQtyOut(remainWaiteQtyOut);
            logGoodsStockList.add(logGoodsStock);
            // 3. 商品库存 GoodsStock
            goodsStock.setQuantityOut(remainQtyOut);
            goodsStock.setWaitQuantityOut(remainWaiteQtyOut);
        }
        // 整合返回
        GoodsStockAndLog goodsStockDto = new GoodsStockAndLog();
        goodsStockDto.setLogGoodsStockList(logGoodsStockList);
        goodsStockDto.setGoodsStockList(goodsStockList);
        return goodsStockDto;
    }


    private LogGoodsStock setLogGoodsStockByBill(LogGoodsStock log,
                                                 Bill bill) {
        log.setBillId(bill.getId());
        log.setStoreId(bill.getStoreId());
        log.setBusinessTime(bill.getBusinessTime());
        return log;
    }

    private LogGoodsStock setLogGoodsStockByBill(LogGoodsStock log,
                                                 BillStock bill) {
        log.setBillId(bill.getId());
        log.setStoreId(bill.getStoreId());
        log.setBusinessTime(bill.getBusinessTime());
        return log;
    }
}
