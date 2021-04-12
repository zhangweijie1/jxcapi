package com.mrguo.service.impl.bill.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillInventoryMapper;
import com.mrguo.dao.bsd.StockMapper;
import com.mrguo.dao.bsd.StoreMapper;
import com.mrguo.vo.bill.BillInventoryAndDetailsVo;
import com.mrguo.entity.bill.*;
import com.mrguo.entity.bsd.Store;
import com.mrguo.vo.bill.BillInventoryDetailVo;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.uils.BillHandleTempPrintServiceImpl;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.service.impl.bill.uils.BillSetDataSetter;
import com.mrguo.service.impl.bill.uils.BillUtilsGenerator;
import com.mrguo.service.impl.log.LogGoodsStockServiceImpl;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName BillInventoryServiceImpl
 * @Description 盘点票据相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service("billInventoryServiceImpl")
public class BillInventoryServiceImpl implements BillBaseService<BillInventory> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillDetailExtInventoryServiceImpl billDetailExtInventoryService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private BillInventoryMapper billInventoryMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private LogGoodsStockServiceImpl logGoodsStockService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 新增盘点单
     *
     * @param billDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillInventoryAndDetailsVo billDto) throws CustomsException {
        Bill bill = billDto.getBill();
        List<BillInventoryDetailVo> billInventoryDetailVoList = billDto.getBillDetailExtInventoryList();
        ArrayList<BillDetail> billDetailList = new ArrayList<>(billInventoryDetailVoList);
        Long userId = (Long) request.getAttribute("userId");
        // 1. 校验
        valiStore(bill.getStoreId());
        // 2. 处理数据并新增（ 票据+明细 ）
        bill.setCreateUserId(userId);
        bill.setUpdateUserId(userId);
        ArrayList<BillDetailExtInventory> billDetailExtInventoryList = new ArrayList<>();
        bill.setBillCat(BillCatEnum.inventory.getCode());
        bill.setBillCatName(BillCatEnum.inventory.getMessage());
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                BillCatEnum.inventory.getCode()));
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        for (BillInventoryDetailVo billInventoryDetailVo : billInventoryDetailVoList) {
            BillDetailExtInventory detailExtInventory = new BillDetailExtInventory();
            detailExtInventory.setId(billInventoryDetailVo.getId());
            detailExtInventory.setBillId(billInventoryDetailVo.getBillId());
            detailExtInventory.setSkuId(billInventoryDetailVo.getSkuId());
            detailExtInventory.setBookQty(billInventoryDetailVo.getBookQty());
            detailExtInventory.setRealQty(billInventoryDetailVo.getRealQty());
            billDetailExtInventoryList.add(detailExtInventory);
        }
        billCommonService.addData(bill, billDetailList);
        billDetailExtInventoryService.insertListData(billDetailExtInventoryList);
        // 库存表
        List<Long> skuIds = billInventoryDetailVoList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> stockArrayList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, bill.getStoreId());
        for (BillInventoryDetailVo detail : billInventoryDetailVoList) {
            List<GoodsStock> collect = stockArrayList.stream().filter(item -> {
                return item.getSkuId().equals(detail.getSkuId());
            }).collect(Collectors.toList());
            GoodsStock goodsStock = collect.get(0);
            // 实际 > 账面 = 盘盈
            BigDecimal realQty = detail.getRealQty();
            BigDecimal bookQty = detail.getBookQty();
            if (realQty.compareTo(bookQty) > 0) {
                BigDecimal dis = realQty.subtract(bookQty);
                goodsStock.setQuantityIn(goodsStock.getQuantityIn().add(dis));
            } else {
                BigDecimal dis = bookQty.subtract(realQty);
                goodsStock.setQuantityOut(goodsStock.getQuantityOut().add(dis));
            }
        }
        //修改库存
        stockMapper.batchUpdateByPrimaryKeySelective(stockArrayList);
        //批量新增盘点日志表
        List<LogGoodsStock> logGoodStockList = getLogGoodStockList(bill, billInventoryDetailVoList);
        logGoodsStockService.addListData(logGoodStockList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancle(Long billId) throws CustomsException {
        Bill bill = billCommonService.getBillById(billId);
        Long storeId = bill.getStoreId();
        if ("1".equals(bill.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
        //
        List<BillDetailExtInventory> detailExtInventoryList = billDetailExtInventoryService.getListDataByMasterId(billId);
        List<Long> skuIds = detailExtInventoryList.stream().map(BillDetailExtInventory::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, storeId);
        if (skuIds.size() != goodsStockList.size()) {
            throw new BusinessException("有商品不存在库存信息");
        }
        for (BillDetailExtInventory detailExtInventory : detailExtInventoryList) {
            for (GoodsStock goodsStock : goodsStockList) {
                BigDecimal realQty = detailExtInventory.getRealQty();
                BigDecimal bookQty = detailExtInventory.getBookQty();
                if (realQty.compareTo(bookQty) > 0) {
                    // 盘盈
                    BigDecimal subtract = realQty.subtract(bookQty);
                    BigDecimal targetQty = goodsStock.getQuantityIn().subtract(subtract);
                    goodsStock.setQuantityIn(targetQty);
                } else {
                    BigDecimal subtract = bookQty.subtract(realQty);
                    BigDecimal targetQty = goodsStock.getQuantityOut().subtract(subtract);
                    goodsStock.setQuantityOut(targetQty);
                }
            }
        }
        goodsStockService.updateListSelectiveData(goodsStockList);
        billCommonService.cancle(billId);
        return Result.ok();
    }


    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.inventory));
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        return null;
    }

    /**
     * 查询盘点单
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listPage(PageParams<BillInventory> pageParams) {
        Page<BillInventory> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.inventory.getCode());
        page.setRecords(billInventoryMapper.listPage(page, data));
        return Result.ok(page);
    }

    public BillInventoryAndDetailsVo getDataByBillId(Long billId) {
        BillInventoryAndDetailsVo billInventoryAndDetailsVo = new BillInventoryAndDetailsVo();
        List<BillInventoryDetailVo> billInventoryDetailVoList = billInventoryMapper.selectDetailByBillId(billId);
        Bill bill = billCommonService.getBillById(billId);
        billInventoryAndDetailsVo.setBillDetailExtInventoryList(billInventoryDetailVoList);
        billInventoryAndDetailsVo.setBill(bill);
        return billInventoryAndDetailsVo;
    }

    public Result getPrintData(Long billId) {
        TempPrintVo print = tempPrintConfig.getInventory();
        Bill bill = billCommonService.getBillById(billId);
        List<BillInventoryDetailVo> billInventoryDetailVoList = billInventoryMapper.selectDetailByBillId(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getInventoryTempPrint(print,
                bill,
                billInventoryDetailVoList);
        return Result.ok(defaultTempPrint);
    }

    private void valiStore(Long storeId) throws CustomsException {
        Store store = storeMapper.selectByPrimaryKey(storeId);
        if (null == store) {
            throw new CustomsException("仓库不存在，请重新选择!");
        }
        // 是否锁定仓库
        if ("0".equals(store.getIsLock())) {
            throw new CustomsException("仓库未锁定，请锁定后再盘点!");
        }
    }

    private List<LogGoodsStock> getLogGoodStockList(Bill bill, List<BillInventoryDetailVo> detailList) {
        ArrayList<LogGoodsStock> logGoodsStocks = new ArrayList<>();
        Date date = new Date();
        for (BillInventoryDetailVo detail : detailList) {
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            BigDecimal bookQty = detail.getBookQty();
            BigDecimal realQty = detail.getRealQty();
            BigDecimal disQty = realQty.subtract(bookQty);
            // set log
            logGoodsStock.setBillId(bill.getId());
            logGoodsStock.setSkuId(detail.getSkuId());
            logGoodsStock.setStoreId(bill.getStoreId());
            logGoodsStock.setBillCat(bill.getBillCat());
            logGoodsStock.setComegoId(bill.getComegoId());
            logGoodsStock.setComegoName(bill.getComegoName());
            logGoodsStock.setBusinessTime(bill.getBusinessTime());
            logGoodsStock.setCreateTime(date);
            if (realQty.compareTo(bookQty) < 0) {
                // 盘亏
                logGoodsStock.setDirection("0");
                logGoodsStock.setQuantity(disQty.negate());
            } else {
                logGoodsStock.setDirection("1");
                logGoodsStock.setQuantity(disQty);
            }
            logGoodsStocks.add(logGoodsStock);
        }
        return logGoodsStocks;
    }
}
