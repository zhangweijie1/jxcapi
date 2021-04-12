package com.mrguo.service.impl.bill.purchase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.*;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.service.impl.bill.stock.BillStockServiceImpl;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import com.mrguo.service.impl.log.LogDebtServiceImpl;
import com.mrguo.service.impl.log.LogGoodsStockServiceImpl;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 进货票据相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service
public class PurchaseBillServiceImpl implements BillBaseService<Bill> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private SupplierServiceImpl supplierService;
    @Autowired
    private BillHandleCostPriceServiceImpl billHandleCostPriceServiceImpl;
    @Autowired
    private LogDebtServiceImpl logDebtService;
    @Autowired
    private LogAmountServiceImpl logAmountService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private LogGoodsStockServiceImpl logGoodsStockService;
    @Autowired
    private BillHandleCostPriceServiceImpl billHandleCostPriceService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;

    /**
     * 新增采购单
     * 处理单据影响的数据
     * 1. 在途库存: 待入库（增加）
     * 2. 商品成本
     * 3. 账户余额: 减少
     * 4. 往来单位欠款: 增加
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillAndDetailsVo<Bill> billAndDetailsVo) throws CustomsException {
        Bill bill = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        // 处理数据
        BillCatEnum purchase = BillCatEnum.purchase;
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(), purchase.getCode()));
        bill.setDirection("1");
        bill.setBillCat(purchase.getCode());
        bill.setBillCatName(purchase.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 1 新增票据+明细
        billCommonService.addData(bill, billDetailList);
        // 2 如果是关联进货单，修改进货单的change_Q数量
        billHandleRelationService.handleRelationChangeQty(bill.getBillRelationId(), billDetailList);
        // 处理单据影响的数据（在途库存，商品成本，账户余额，往来单位欠款）
        // a. 在途库存和log
        List<Long> skuids = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> stockArrayList = goodsStockService.listStockBySkuIdsAndStoreId(skuids, bill.getStoreId());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        GoodsStockAndLog stockDto = billSkuQtyComputer.getDefaultAddWaiteQty(bill, billDetailList, stockArrayList);
        List<GoodsStock> goodsStockList = stockDto.getGoodsStockList();
        List<LogGoodsStock> logGoodsStockList = stockDto.getLogGoodsStockList();
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList);
        logGoodsStockService.addListData(logGoodsStockList);
        // b. 成本日志
        billHandleCostPriceService.computedDefaultCostPrice(bill, billDetailList);
        // c. 账户金额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(),
                bill.getAmountPaid().negate());
        BigDecimal remainAmount = account.getAmount().add(account.getOriginAmount());
        logAmountService.addDataByBill(bill, remainAmount, "0");
        // d. 往来单位欠款
        BigDecimal thisDebt = bill.getAmountDebt();
        Supplier supplier = supplierService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = supplier.getOriginDebt().add(supplier.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "0");
        return Result.ok();
    }

    /**
     * 作废进货单据
     * 回滚默认单据影响的数据：
     * 1. 在途库存: 待入库 (减少)
     * 2. 商品成本
     * 3. 账户余额: 增加余额
     * 4. 往来单位欠款：减少欠款
     *
     * @param billId
     * @return
     * @throws CustomsException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancle(Long billId) throws CustomsException {
        if (billStockService.countRelationIdByBillId(billId) > 0) {
            throw new CustomsException("该单据已有转入库单，请先作废对应的入库单！");
        }
        Bill bill = billCommonService.getBillById(billId);
        if ("1".equals(bill.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
        // 修改stock的待入库数量
        List<BillDetail> detailPurchaseList = billDetailService.listDataByBillId(billId);
        // 修改bill状态
        billCommonService.cancle(billId);
        Long relationId = bill.getBillRelationId();
        if (relationId == null) {
            goodsStockUpdateService.updateStockWaite(bill.getStoreId(),
                    detailPurchaseList, "in", "sub");
        } else {
            Bill relationBill = billCommonService.getBillById(relationId);
            switch (relationBill.getBillCat()) {
                default:
                    // 如果是关联的单据（从进货订单转化的，需要修改renlationBill的change）
                    billHandleRelationService.rollBackChangeQty(bill.getBillRelationId(), detailPurchaseList);
                    // 回滚默认单据影响的数据（在途库存，商品成本，账户余额，往来单位欠款）
                    // a. 在途库存
                    goodsStockUpdateService.updateStockWaite(bill.getStoreId(),
                            detailPurchaseList, "in", "sub");

                    break;
            }
        }
        // c. 账户金额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(),
                bill.getAmountPaid());
        BigDecimal remainAmount = account.getAmount().add(account.getOriginAmount());
        logAmountService.addDataByBill(bill, remainAmount, "1");
        // d. 往来单位欠款
        BigDecimal thisDebt = bill.getAmountDebt().negate();
        Supplier supplier = supplierService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = supplier.getOriginDebt().add(supplier.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "1");
        // 重新计算成本
        billHandleCostPriceServiceImpl.computedAllCostPriceAfterBill(bill);
        return Result.ok();
    }

    public BillAndDetailsVo<Bill> getBillAndDetailById(Long billId) throws IOException {
        return billCommonService.getBillAndDetailsById(billId);
    }

    /**
     * 查询进货单列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listPage(PageParams<Bill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Bill> page = pageParams.getPage();
        data.put("billCat", BillCatEnum.purchase.getCode());
        IPage<Bill> billPage = billCommonService.listPage(page, data);
        return Result.ok(billPage);
    }

    /**
     * 查询可以转退货的单据
     * 退还qty小于商品qyu： returnQty < qty
     *
     * @param pageParams
     * @return
     * @throws IOException
     */
    public IPage<Bill> listWaite2return(PageParams<Bill> pageParams) {
        return billCommonService.listWaiteReturnBillsPage(pageParams, BillCatEnum.purchase);
    }

    /**
     * 获取单据编号：BillNo
     *
     * @return
     * @throws CustomsException
     */
    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.purchase));
    }

    /**
     * 获取打印数据
     * 1. 先获取模板
     *
     * @return
     */
    public Result getPrintData(Long billId) {
        TempPrintVo purchase = tempPrintConfig.getPurchase();
        BillAndDetailsVo<Bill> billBillAndDetailsVo = billCommonService.getBillAndDetailsById(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(purchase, billBillAndDetailsVo);
        return Result.ok(defaultTempPrint);
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        return null;
    }
}
