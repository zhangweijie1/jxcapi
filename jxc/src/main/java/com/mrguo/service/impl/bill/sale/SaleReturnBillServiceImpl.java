package com.mrguo.service.impl.bill.sale;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.basedata.CustomerServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售单相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service
public class SaleReturnBillServiceImpl implements BillBaseService<Bill> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private AccountServiceImpl accountService;
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
     * 新增销售退货单
     * 处理单据影响的数据
     * 1. 在途库存: 待出库（减少）
     * 2. 商品成本
     * 3. 账户余额: 减少
     * 4. 往来单位欠款: 减少
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillAndDetailsVo<Bill> billAndDetailsVo) throws CustomsException {
        Bill bill = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        // 处理数据
        BillCatEnum saleReturn = BillCatEnum.sale_return;
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                saleReturn.getCode()));
        bill.setDirection("1");
        bill.setBillCat(saleReturn.getCode());
        bill.setBillCatName(saleReturn.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 1 新增票据+明细
        billCommonService.addData(bill, billDetailList);
        // 2 如果是关联销售单，修改销售单的return_qty数量
        billHandleRelationService.handleRelationReturnQty(bill.getBillRelationId(), billDetailList);
        // 处理单据影响的数据（在途库存，商品成本，账户余额，往来单位欠款）
        // a. 在途库存
        List<Long> skuids = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> stockArrayList = goodsStockService.listStockBySkuIdsAndStoreId(skuids, bill.getStoreId());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        GoodsStockAndLog stockDto = billSkuQtyComputer.getDefaultAddWaiteQty(bill, billDetailList, stockArrayList);
        List<GoodsStock> goodsStockList = stockDto.getGoodsStockList();
        List<LogGoodsStock> logGoodsStockList = stockDto.getLogGoodsStockList();
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList);
        logGoodsStockService.addListData(logGoodsStockList);
        // b. 商品成本
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsStock> remainGoodsStocks = goodsStockService.listStockMergeStoreBySkuIds(skuIds);
        billHandleCostPriceService.setCurrentCostPriceLogs( bill, remainGoodsStocks, billDetailList);
        // c. 账户余额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(),
                bill.getAmountPaid().negate());
        BigDecimal remainAmount = account.getAmount().add(account.getOriginAmount());
        logAmountService.addDataByBill(bill, remainAmount, "0");
        // d. 客户欠款
        BigDecimal thisDebt = bill.getAmountDebt().negate();
        Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "0");
        return Result.ok(bill);
    }

    /**
     * 作废sale单据
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
        // 修改bill 的iscancle
        billCommonService.cancle(billId);
        // ===== 回滚默认单据影响的数据 =====
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        // 如果是关联的单据（从销售单转化的，需要修改renlationBill的return_qty）
        billHandleRelationService.rollBackReturnQty(bill.getBillRelationId(), billDetailList);
        // 1. 在途库存
        goodsStockUpdateService.updateStockWaite(bill.getStoreId(),
                billDetailList, "in", "sub");
        // 2. 商品成本
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsStock> remainGoodsStocks = goodsStockService.listStockMergeStoreBySkuIds(skuIds);
        billHandleCostPriceService.setCurrentCostPriceLogs(
                bill,
                remainGoodsStocks,
                billDetailList);
        // 3. 账户余额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(), bill.getAmountPaid());
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByBill(bill, remainAmount, "1");
        // 4. 往来单位欠款
        BigDecimal thisDebt = bill.getAmountDebt();
        Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "1");
        return Result.ok();
    }

    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.sale_return));
    }

    @Override
    public Result listPage(PageParams<Bill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Bill> page = pageParams.getPage();
        data.put("billCat", BillCatEnum.sale_return.getCode());
        return Result.ok(billCommonService.listPage(page, data));
    }

    @Override
    public Result export(Long billId) {
        TempPrintVo print = tempPrintConfig.getSaleOrder();
        BillAndDetailsVo<Bill> billBillAndDetailsVo = billCommonService.getBillAndDetailsById(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(print, billBillAndDetailsVo);
        return Result.ok(defaultTempPrint);
    }

    @Override
    public Result print(Long billId) {
        return null;
    }

    /**
     * 获取可退货的销售单
     *
     * @param pageParams
     * @return
     */
    public Result listWaiteReturnBillsPage(PageParams<Bill> pageParams) {
        return Result.ok(billCommonService.listWaiteReturnBillsPage(pageParams, BillCatEnum.sale));
    }
}
