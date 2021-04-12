package com.mrguo.service.impl.bill.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillSaleMapper;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.vo.bill.BillSaleAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bill.BillExtSale;
import com.mrguo.entity.bill.BillSale;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.service.impl.bill.stock.BillStockServiceImpl;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import com.mrguo.service.impl.log.LogDebtServiceImpl;
import com.mrguo.service.impl.basedata.CustomerServiceImpl;
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
 * @Description 销售单相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service("billSaleServiceImpl")
public class SaleBillServiceImpl implements BillBaseService<BillSale> {

    @Autowired
    private BillSaleMapper billSaleMapper;
    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private BillSaleExtendServiceImpl billSaleExtendService;
    @Autowired
    private CustomerServiceImpl customerService;
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
     * 新增销售单（需要出库）
     * 处理单据影响的数据
     * 1. 在途库存: 待出库（增加）
     * 2. 商品成本: 加个记录而已
     * 3. 账户余额: 增加
     * 4. 往来单位欠款: 增加
     *
     * @param billDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public <E extends BillAndDetailsVo<Bill>> Result addData(BillSaleAndDetailsVo billDto) throws CustomsException {
        Bill bill = billDto.getBill();
        List<BillDetail> billDetailList = billDto.getBillDetailList();
        // 处理数据
        BillCatEnum billCat = BillCatEnum.sale;
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                billCat.getCode()));
        bill.setDirection("0");
        bill.setBillCat(billCat.getCode());
        bill.setBillCatName(billCat.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 1 新增票据+明细
        billCommonService.addData(bill, billDetailList);
        // 2 如果是关联销售订单，修改进货单的change_qty
        billHandleRelationService.handleRelationChangeQty(bill.getBillRelationId(), billDetailList);
        // 新增扩展表
        BillExtSale billExtSale = billDto.getBillExtSale() == null ?
                new BillExtSale() : billDto.getBillExtSale();
        addSaleExtend(bill, billExtSale);
        // 处理单据影响的数据（在途库存，商品成本，账户余额，往来单位欠款）
        // a. 在途库存
        List<Long> skuids = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> stockArrayList = goodsStockService.listStockBySkuIdsAndStoreId(skuids, bill.getStoreId());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        GoodsStockAndLog stockDto = billSkuQtyComputer.getDefaultSubWaiteQty(bill, billDetailList, stockArrayList);
        List<GoodsStock> goodsStockList = stockDto.getGoodsStockList();
        List<LogGoodsStock> logGoodsStockList = stockDto.getLogGoodsStockList();
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList);
        logGoodsStockService.addListData(logGoodsStockList);
        // b. 商品成本
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsStock> remainGoodsStocks = goodsStockService.listStockMergeStoreBySkuIds(skuIds);
        billHandleCostPriceService.setCurrentCostPriceLogs(
                bill,
                remainGoodsStocks,
                billDetailList);
        // c. 账户金额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(),
                bill.getAmountPaid());
        BigDecimal remainAmount = account.getAmount().add(account.getOriginAmount());
        logAmountService.addDataByBill(bill, remainAmount, "0");
        // d. 往来单位欠款
        BigDecimal thisDebt = bill.getAmountDebt();
        Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "0");
        return Result.ok(bill);
    }


    /**
     * 作废销售单据
     * 回滚默认单据影响的数据：
     * 1. 在途库存: 待出库 (减少)
     * 2. 商品成本
     * 3. 账户余额: 减少余额
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
            return Result.fail("该单据已有转出库单，请先作废对应的出库单！");
        }
        Bill bill = billCommonService.getBillById(billId);
        if ("1".equals(bill.getIsCancle())) {
            return Result.fail("该单据已经作废！");
        }
        // 修改bill的iscancle
        billCommonService.cancle(billId);
        List<BillDetail> detailSaleList = billDetailService.listDataByBillId(billId);
        Long relationId = bill.getBillRelationId();
        if (relationId == null) {
            goodsStockUpdateService.updateStockWaite(bill.getStoreId(),
                    detailSaleList, "out", "sub");
        } else {
            Bill relationBill = billCommonService.getBillById(relationId);
            switch (relationBill.getBillCat()) {
                case "borrow_out":
                    billHandleRelationService.rollBackChangeQty2(bill.getBillRelationId(), detailSaleList);
                    break;
                default:
                    billHandleRelationService.rollBackChangeQty(bill.getBillRelationId(), detailSaleList);
                    // 回滚默认单据影响的数据（在途库存，商品成本，账户余额，往来单位欠款）
                    // a. 在途库存
                    goodsStockUpdateService.updateStockWaite(bill.getStoreId(),
                            detailSaleList, "out", "sub");
                    break;
            }
        }
        // b. 商品成本，无需回滚
        // c. 账户余额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(), bill.getAmountPaid().negate());
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByBill(bill, remainAmount, "1");
        // d. 往来单位欠款
        BigDecimal thisDebt = bill.getAmountDebt().negate();
        Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addDataByBill(bill, remainDebt, "1");
        return Result.ok();
    }

    /**
     * 查询销售单详情
     *
     * @param billId
     * @return
     */
    public BillSaleAndDetailsVo getSaleDataById(Long billId) {
        BillAndDetailsVo<Bill> billContainGoodsData = billCommonService.getBillAndDetailsById(billId);
        BillExtSale billExtSale = billSaleExtendService.getDataById(billId);
        BillSaleAndDetailsVo billSaleDto = new BillSaleAndDetailsVo();
        billSaleDto.setBillExtSale(billExtSale);
        billSaleDto.setBill(billContainGoodsData.getBill());
        billSaleDto.setBillDetailList(billContainGoodsData.getBillDetailList());
        return billSaleDto;
    }

    public List<BillDetail> listNotTransDetailById(Long billId) {
        return billDetailService.listWaiteTransDataByBillId(billId);
    }

    public List<BillDetail> listHasTransDataById(Long billId) {
        return billDetailService.listHasTransDataByBillId(billId);
    }

    /**
     * 查询销售单
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listPage(PageParams<BillSale> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<BillSale> page = pageParams.getPage();
        page.setRecords(billSaleMapper.listCustom(page, data));
        return Result.ok(page);
    }

    /**
     * 查询待退货（已入库部分）
     *
     * @param pageParams
     * @return
     */
    public IPage<Bill> listWaiteReturnBillsPage(PageParams<Bill> pageParams) throws IOException {
        return billCommonService.listWaiteReturnBillsPage(pageParams, BillCatEnum.sale);
    }

    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.sale));
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        return null;
    }

    public Result getPrintData(Long billId) {
        TempPrintVo print = tempPrintConfig.getSaleOrder();
        BillAndDetailsVo<Bill> billBillAndDetailsVo = billCommonService.getBillAndDetailsById(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(print, billBillAndDetailsVo);
        return Result.ok(defaultTempPrint);
    }

    /**
     * list_stock
     * 新增扩展表
     */
    private void addSaleExtend(Bill bill, BillExtSale billExtSale) {
        billExtSale.setBillId(bill.getId());
        billSaleExtendService.saveData(billExtSale);
        // 同步客户信息
        syncCustomerBySale(billExtSale, bill.getComegoId());
    }

    /**
     * 根据销售单 同步修改客户信息
     */
    void syncCustomerBySale(BillExtSale billExtSale, Long customerId) {
        if ("1".equals(billExtSale.getIsSync())) {
            Customer customer = new Customer();
            customer.setId(customerId);
            customer.setContacter(billExtSale.getContacter());
            customer.setPhone(billExtSale.getPhone());
            customer.setAddress(billExtSale.getDeliveryAddress());
            customerService.updateData(customer);
        }
    }
}
