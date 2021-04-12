package com.mrguo.service.impl.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.log.LogStockMapper;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
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
 * @Description
 * @date 2020/1/410:41 PM
 * @updater 郭成兴
 * @updatedate 2020/1/410:41 PM
 */
@Service
public class LogGoodsStockServiceImpl extends BaseServiceImpl<LogGoodsStock> {

    @Autowired
    private LogStockMapper logStockMapper;
    @Autowired
    GoodsStockServiceImpl goodsStockService;

    @Override
    public MyMapper<LogGoodsStock> getMapper() {
        return logStockMapper;
    }

    @Override
    public LogGoodsStock saveData(LogGoodsStock entity) {
        return super.saveData(entity);
    }

    /**
     * 批量新增库存日志
     *
     * @param logGoodsStockList
     * @throws CustomsException
     */
    public void addListData(List<LogGoodsStock> logGoodsStockList) throws CustomsException {
        if (logStockMapper.insertList(logGoodsStockList) < 0) {
            throw new CustomsException("批量新增库存日志失败！");
        }
    }

    public IPage<LogGoodsStock> getStockLogByGood(PageParams<LogGoodsStock> pageParams) {
        Page<LogGoodsStock> page = pageParams.getPage();
        page.setRecords(logStockMapper.selectStockLogBySkuId(page, pageParams.getData()));
        return page;
    }

    public List<LogGoodsStock> getLastDataByBusinessTimeGroupSku(Date businessTime) {
        return logStockMapper.getLastDataByBusinessTimeGroupSku(businessTime);
    }

    /**
     * 获取最新的库存数据
     *
     * @param detailList
     * @return
     */
    private List<LogGoodsStock> getLastStockLogs(BillCatEnum billCatEnum, List<BillDetail> detailList) throws CustomsException {
        // 获取当前库存
        List<GoodsStock> goodsStockList = listStocksByBillDetailList(detailList);
        // 计算此单据后的库存
        Date date = new Date();
        ArrayList<LogGoodsStock> logGoodsStocks = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {

        }
        for (BillDetail detail : detailList) {
            for (GoodsStock stock : goodsStockList) {
                if (detail.getSkuId().equals(stock.getSkuId())) {
                    LogGoodsStock stockLog = new LogGoodsStock();
                    stockLog.setSkuId(detail.getSkuId());
                    stockLog.setBillId(detail.getBillId());
                    stockLog.setStoreId(detail.getStoreId());
                    stockLog.setCreateTime(date);
                    // 计算余量
                    BigDecimal remQtyIn = stock.getQuantityIn();
                    BigDecimal remQtyOut = stock.getQuantityIn();
                    BigDecimal remWaiteQtyIn = stock.getWaitQuantityIn();
                    BigDecimal remWaiteQtyOut = stock.getWaitQuantityOut();
                    BigDecimal unitMulti = detail.getUnitMulti() == null ? new BigDecimal(1) : detail.getUnitMulti();
                    BigDecimal thisTimeQty = detail.getQuantity().multiply(unitMulti);
                    if (BillCatEnum.sale.equals(billCatEnum)) {
                        remWaiteQtyOut = remWaiteQtyOut.add(thisTimeQty);
                    }
                    if (BillCatEnum.purchase.equals(billCatEnum)) {
                        remWaiteQtyIn = remWaiteQtyIn.add(thisTimeQty);
                    }
                    stockLog.setRemQtyIn(remQtyIn);
                    stockLog.setRemQtyOut(remQtyOut);
                    stockLog.setRemWaiteQtyIn(remWaiteQtyIn);
                    stockLog.setRemWaiteQtyOut(remWaiteQtyOut);
                    logGoodsStocks.add(stockLog);
                }
            }
        }
        return logGoodsStocks;
    }

    private LogGoodsStock getGoodStockLog(List<BillDetail> detailList) {
        return null;
    }


    /**
     * 根据billDetail（包含sku_id）获取多个sku的库存信息
     *
     * @param detailList
     * @return
     */
    List<GoodsStock> listStocksByBillDetailList(List<BillDetail> detailList) {
        List<Long> skuIds = detailList.stream().map(
                item -> {
                    return item.getSkuId();
                }).collect(Collectors.toList());
        return goodsStockService.listStockMergeStoreBySkuIds(skuIds);
    }
}
