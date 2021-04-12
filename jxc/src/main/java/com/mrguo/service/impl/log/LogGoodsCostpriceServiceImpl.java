package com.mrguo.service.impl.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.LogGoodsCostPriceMapper;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.goods.GoodsCostPrice;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.log.LogGoodsCostPrice;
import com.mrguo.service.impl.bill.uils.BillUtilsGenerator;
import com.mrguo.service.impl.bill.uils.BillSkuQtyComputer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName LogGoodsCostpriceServiceImpl
 * @Description 商品成本价明细
 * @date 2020/3/254:07 PM
 * @updater 郭成兴
 * @updatedate 2020/3/254:07 PM
 */

@Service
public class LogGoodsCostpriceServiceImpl extends BaseServiceImpl<LogGoodsCostPrice> {

    @Autowired
    private LogGoodsCostPriceMapper logGoodsCostPriceMapper;

    @Override
    public MyMapper getMapper() {
        return logGoodsCostPriceMapper;
    }

    /**
     * 查询某商品成本价格明细
     *
     * @param pageParams
     * @return
     */
    public IPage<LogGoodsCostPrice> listDataBySkuId(PageParams<LogGoodsCostPrice> pageParams) {
        Page<LogGoodsCostPrice> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(logGoodsCostPriceMapper.listDataBySkuId(page, data));
        return page;
    }

    /**
     * 新增成本明细
     * 需要的变量 = 商品skuId，(进出数量,单位系数)，此次成本价
     * 销售单，进货单等都需要执行此方法
     * 此方法执行后，同时会更新sku的成本
     *
     * @param logGoodsCostPriceList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void addListData(List<LogGoodsCostPrice> logGoodsCostPriceList) throws CustomsException {
        if (logGoodsCostPriceMapper.insertList(logGoodsCostPriceList) < 1) {
            throw new CustomsException("添加成本价日志失败！");
        }
    }

    /**
     * @param billId
     * @param billCat
     * @param billCatName
     * @param businessTime
     * @param detailList               当前BillDetails： 获取当前数量
     * @param oldCostAndStockPriceList 原有costPrice： 获取原有成本价
     * @param remainGoodsCostPrices    结余costPrice： 获取结余成本价
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addListDataBySkus(Long billId,
                                  String billCat,
                                  String billCatName,
                                  Date businessTime,
                                  List<BillDetail> detailList,
                                  List<GoodsCostPrice> oldCostAndStockPriceList,
                                  List<GoodsCostPrice> remainGoodsCostPrices) throws CustomsException {
        _addListDataBySkus(
                billId,
                billCat,
                billCatName,
                businessTime,
                detailList,
                oldCostAndStockPriceList,
                null,
                remainGoodsCostPrices);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addListDataBySkus(Long billId,
                                  String billCat,
                                  String billCatName,
                                  Date businessTime,
                                  List<BillDetail> detailList,
                                  List<GoodsCostPrice> oldCostAndStockPriceList,
                                  List<GoodsStock> remainGoodsStockList,
                                  List<GoodsCostPrice> remainGoodsCostPrices) throws CustomsException {
        _addListDataBySkus(
                billId,
                billCat,
                billCatName,
                businessTime,
                detailList,
                oldCostAndStockPriceList,
                remainGoodsStockList,
                remainGoodsCostPrices);
    }

    private void _addListDataBySkus(Long billId,
                                    String billCat,
                                    String billCatName,
                                    Date businessTime,
                                    List<BillDetail> detailList,
                                    List<GoodsCostPrice> oldCostAndStockPriceList,
                                    List<GoodsStock> remainGoodsStockList,
                                    List<GoodsCostPrice> remainGoodsCostPrices) throws CustomsException {
        ArrayList<LogGoodsCostPrice> logGoodsCostPrices = new ArrayList<>();
        Date date = new Date();
        List<Long> skuIds = detailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        for (Long skuId : skuIds) {
            LogGoodsCostPrice logGoodsCostPrice = new LogGoodsCostPrice();
            logGoodsCostPrice.setSkuId(skuId);
            logGoodsCostPrice.setBillId(billId);
            logGoodsCostPrice.setBusinessTime(businessTime);
            logGoodsCostPrice.setCreateTime(date);
            logGoodsCostPrice.setBillCat(billCat);
            logGoodsCostPrice.setBillCatName(billCatName);
            // 当次数量

            BigDecimal qtyInBaseUnit = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(skuId, detailList);
            logGoodsCostPrice.setQuantity(qtyInBaseUnit);
            // 当次成本价
            GoodsCostPrice oldGoodsCostPrice = oldCostAndStockPriceList.stream().filter(item -> {
                return item.getSkuId().equals(skuId);
            }).collect(Collectors.toList()).get(0);
            logGoodsCostPrice.setPriceCost(oldGoodsCostPrice.getPriceCost());
            // 结余数量 = 原有qty + 本次qty
            // 或者就等于参数
            if (remainGoodsStockList == null) {
                BigDecimal remainQty = oldGoodsCostPrice.getQty().add(qtyInBaseUnit);
                logGoodsCostPrice.setRemainQty(remainQty);
            } else {
                GoodsStock goodsStock = remainGoodsStockList.stream().filter(item -> {
                    return skuId.equals(item.getSkuId());
                }).collect(Collectors.toList()).get(0);
                BigDecimal remainQty = goodsStock.getOriginQuantity()
                        .add(goodsStock.getQuantityIn())
                        .add(goodsStock.getWaitQuantityIn())
                        .subtract(goodsStock.getQuantityOut())
                        .subtract(goodsStock.getWaitQuantityOut());
                logGoodsCostPrice.setRemainQty(remainQty);
            }
            // 结余成本价
            GoodsCostPrice remainGoodsCostPrice = remainGoodsCostPrices.stream().filter(item -> {
                return item.getSkuId().equals(skuId);
            }).collect(Collectors.toList()).get(0);
            // 结余成本就是参数中的成本（已预先处理好）
            logGoodsCostPrice.setRemainPriceCost(remainGoodsCostPrice.getPriceCost());
            logGoodsCostPrices.add(logGoodsCostPrice);
        }
        addListData(logGoodsCostPrices);
    }

    /**
     * 获取上次的成本,包含不同的sku
     *
     * @param businessTime
     * @return
     */
    public List<LogGoodsCostPrice> getLastDataByBusinessTime(Date businessTime) {
        return logGoodsCostPriceMapper.getLastDataByBusinessTime(businessTime);
    }

    public List<LogGoodsCostPrice> getDataAfterBusinessTimeBySkuIds(Date businessTime, List<Long> skuIds) {
        List<String> skuStrIds = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        return logGoodsCostPriceMapper.getDataAfterBusinessTimeBySkuIds(businessTime, skuStrIds);
    }
}
