package com.mrguo.service.impl.bill.assembel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillAssembelMapper;
import com.mrguo.dao.bsd.StockMapper;
import com.mrguo.dao.log.LogStockMapper;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bill.BillExtAssembel;
import com.mrguo.vo.bill.BillAssembelVo;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.service.inter.bill.BillCommonService;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName 组装拆卸
 * @Description
 * @date 2020/4/165:39 PM
 * @updater 郭成兴
 * @updatedate 2020/4/165:39 PM
 */
@Service
public class AssembelBillServiceImpl implements BillBaseService<BillAssembelVo> {

    @Autowired
    BillCommonService billCommonService;
    @Autowired
    private BillAssembelMapper billAssembelMapper;
    @Autowired
    private BillCommonServiceImpl billService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillExtAssembelServiceImpl billExtAssembelService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private LogStockMapper logStockMapper;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private BillHandleCostPriceServiceImpl billHandleCostPriceService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;

    /**
     * 新增组装单
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillAndDetailsVo<BillAssembelVo> billAndDetailsVo) throws CustomsException {
        BillAssembelVo bill = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        Date date = new Date();
        // 校验
        // 1. 校验仓库是否锁定
        billValidationServiceImpl.valiStoreIsLock(bill.getStoreIdIn());
        billValidationServiceImpl.valiStoreIsLock(bill.getStoreIdOut());
        //
        BillCatEnum assemble = BillCatEnum.assemble;
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                assemble.getCode()));
        bill.setBillCat(assemble.getCode());
        bill.setBillCatName(assemble.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 新增单据（t_bill and t_bill_assemble）
        List<BillDetail> billDetailListIn = billDetailList.stream().filter(item -> {
            return "1".equals(item.getDirection());
        }).collect(Collectors.toList());
        List<BillDetail> billDetailListOut = billDetailList.stream().filter(item -> {
            return "0".equals(item.getDirection());
        }).collect(Collectors.toList());
        //
        billCommonService.addData(bill, billDetailList);
        BillExtAssembel billExtAssembel = new BillExtAssembel();
        billExtAssembel.setId(bill.getId());
        billExtAssembel.setStoreIdIn(bill.getStoreIdIn());
        billExtAssembel.setStoreIdOut(bill.getStoreIdOut());
        BillSkuNamesComputer billSkuNamesComputer = BillUtilsGenerator.getBillSkuNamesComputer();
        String goodsNameIn = billSkuNamesComputer.getSkuNamesOfDetailList(billDetailListIn);
        String goodsNameOut = billSkuNamesComputer.getSkuNamesOfDetailList(billDetailListOut);
        billExtAssembel.setGoodsNameIn(goodsNameIn);
        billExtAssembel.setGoodsNameOut(goodsNameOut);
        billExtAssembelService.saveData(billExtAssembel);
        //修改t_stock表
        List<Long> skuIdsOut = billDetailListOut.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<Long> skuIdsIn = billDetailListIn.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockOutList = goodsStockService.listStockBySkuIdsAndStoreId(skuIdsOut, bill.getStoreIdOut());
        List<GoodsStock> goodsStockInList = goodsStockService.listStockBySkuIdsAndStoreId(skuIdsIn, bill.getStoreIdIn());
        List<LogGoodsStock> logGoodsStocks = new ArrayList<>(billDetailList.size());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        for (GoodsStock goodsStock : goodsStockOutList) {
            BigDecimal targetQty = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), billDetailList);
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setBillId(bill.getId());
            logGoodsStock.setQuantity(targetQty);
            logGoodsStock.setDirection("0");
            logGoodsStock.setCreateTime(date);
            logGoodsStocks.add(logGoodsStock);
            //
            goodsStock.setQuantityOut(goodsStock.getQuantityOut().add(targetQty));
        }
        for (GoodsStock goodsStock : goodsStockInList) {
            BigDecimal targetQty = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), billDetailList);
            LogGoodsStock logGoodsStock = new LogGoodsStock();
            logGoodsStock.setSkuId(goodsStock.getSkuId());
            logGoodsStock.setBillId(bill.getId());
            logGoodsStock.setQuantity(targetQty);
            logGoodsStock.setDirection("1");
            logGoodsStock.setCreateTime(date);
            logGoodsStocks.add(logGoodsStock);
            //
            goodsStock.setQuantityIn(goodsStock.getQuantityIn().add(targetQty));
        }
        ArrayList<GoodsStock> goodsStockParams = new ArrayList<>(goodsStockOutList);
        goodsStockParams.addAll(goodsStockInList);
        stockMapper.batchUpdateByPrimaryKeySelective(goodsStockParams);
        logStockMapper.insertList(logGoodsStocks);
        // 入库产品修改成本价
        billHandleCostPriceService.computedDefaultCostPrice(bill, billDetailListIn);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancle(Long billId) throws CustomsException {
        // 查询需要的数据
        Bill bill = billService.selectByPrimaryKey(billId);
        if ("1".equals(bill.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        BillExtAssembel billExtAssembel = billExtAssembelService.selectByPrimaryKey(billId);
        Long storeIdIn = billExtAssembel.getStoreIdIn();
        Long storeIdOut = billExtAssembel.getStoreIdOut();
        //
        List<BillDetail> detailListIn = detailList.stream().filter(item -> {
            return "1".equals(item.getDirection());
        }).collect(Collectors.toList());
        List<BillDetail> detailListOut = detailList.stream().filter(item -> {
            return "0".equals(item.getDirection());
        }).collect(Collectors.toList());
        //
        List<Long> skuInIds = detailListIn.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<Long> skuOutIds = detailListOut.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsStock> goodsStockListIn = goodsStockService.listStockBySkuIdsAndStoreId(skuInIds, storeIdIn);
        List<GoodsStock> goodsStockListOut = goodsStockService.listStockBySkuIdsAndStoreId(skuOutIds, storeIdOut);
        if (goodsStockListIn.size() != skuInIds.size()) {
            throw new BusinessException("新商品中，有商品没库存信息");
        }
        if (goodsStockListOut.size() != skuOutIds.size()) {
            throw new BusinessException("原商品中，有商品没库存信息");
        }
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        for (GoodsStock goodsStockIn : goodsStockListIn) {
            BigDecimal qtyInBase = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStockIn.getSkuId(), detailListIn);
            BigDecimal targetQty = goodsStockIn.getQuantityIn().subtract(qtyInBase);
            goodsStockIn.setQuantityIn(targetQty);
        }
        for (GoodsStock goodsStockOut : goodsStockListOut) {
            BigDecimal qtyInBase = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStockOut.getSkuId(), detailListOut);
            BigDecimal targetQty = goodsStockOut.getQuantityOut().subtract(qtyInBase);
            goodsStockOut.setQuantityOut(targetQty);
        }
        ArrayList<GoodsStock> goodsStocks = new ArrayList<>();
        goodsStocks.addAll(goodsStockListIn);
        goodsStocks.addAll(goodsStockListOut);
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStocks);
        billCommonService.cancle(billId);
        return Result.ok();
    }

    /**
     * 查询组装单
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listPage(PageParams<BillAssembelVo> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<BillAssembelVo> page = pageParams.getPage();
        page.setRecords(billAssembelMapper.selectListData(page, data));
        return Result.ok(page);
    }

    public BillAndDetailsVo<BillAssembelVo> getBillAndDetailByBillId(Long billId) throws IOException {
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        BillAssembelVo billAssembelVo = billAssembelMapper.selectOneByBillId(billId);
        BillAndDetailsVo<BillAssembelVo> billAndDetailsVo = new BillAndDetailsVo<>();
        billAndDetailsVo.setBillDetailList(billDetailList);
        billAndDetailsVo.setBill(billAssembelVo);
        return billAndDetailsVo;
    }

    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.assemble));
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        TempPrintVo print = tempPrintConfig.getAssembel();
        BillAssembelVo billAssembelVo = billAssembelMapper.selectOneByBillId(billId);
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(print,
                billAssembelVo, billDetailList);
        return Result.ok(defaultTempPrint);
    }
}
