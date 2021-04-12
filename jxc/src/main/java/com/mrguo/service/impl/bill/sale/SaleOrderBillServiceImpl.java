package com.mrguo.service.impl.bill.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.vo.bill.BillSaleAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bill.BillExtSale;
import com.mrguo.service.inter.bill.BillAuditService;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.service.inter.bill.BillCommonService;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.service.impl.bill.uils.BillHandleTempPrintServiceImpl;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.service.impl.bill.uils.BillSetDataSetter;
import com.mrguo.service.impl.bill.uils.BillUtilsGenerator;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 销售订单相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service
public class SaleOrderBillServiceImpl implements BillBaseService<Bill>,
        BillAuditService {

    @Autowired
    private BillCommonService billCommonService;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    @Qualifier("billAuditServiceImpl")
    private BillAuditService billAuditService;
    @Autowired
    private SaleBillServiceImpl billSaleService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private BillSaleExtendServiceImpl billSaleExtendService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;
    @Autowired
    private HttpServletRequest request;

    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillSaleAndDetailsVo billDto) throws CustomsException {
        Bill bill = billDto.getBill();
        List<BillDetail> billDetailList = billDto.getBillDetailList();
        billDetailList.forEach(item -> {
            item.setChangeQuantity(BigDecimal.ZERO);
            item.setChangeQuantity2(BigDecimal.ZERO);
        });
        // 处理数据
        BillCatEnum sale = BillCatEnum.sale_order;
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                sale.getCode()));
        bill.setDirection("0");
        bill.setBillCat(sale.getCode());
        bill.setBillCatName(sale.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 新增
        billCommonService.addData(bill, billDetailList);
        // 新增扩展表
        addSaleExtend(bill, billDto.getBillExtSale());
        return Result.ok(bill);
    }

    @Override
    public Result cancle(Long billId) throws CustomsException {
        return close(billId);
    }

    @Override
    public Result getBillNo() {
        try {
            return Result.ok(billOrderNoService.getBillCode(BillCatEnum.sale_order.getCode()));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result listPage(PageParams<Bill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Bill> page = pageParams.getPage();
        data.put("billCat", BillCatEnum.sale_order.getCode());
        IPage<Bill> billPage = billCommonService.listPage(page, data);
        return Result.ok(billPage);
    }

    @Override
    public Result passAuditList(List<Long> billIds) {
        return billAuditService.passAuditList(billIds);
    }

    @Override
    public Result antiAuditList(List<Long> billIds) {
        return billAuditService.antiAuditList(billIds);
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        TempPrintVo print = tempPrintConfig.getSaleOrder();
        BillAndDetailsVo<Bill> billBillAndDetailsVo = billCommonService.getBillAndDetailsById(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(print, billBillAndDetailsVo);
        return Result.ok(defaultTempPrint);
    }

    /**
     * 修改销售订单
     *
     * @param billDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateData(BillSaleAndDetailsVo billDto) throws CustomsException {
        Bill bill = billDto.getBill();
        List<BillDetail> billDetailList = billDto.getBillDetailList();
        bill.setBillCat(BillCatEnum.sale_order.getCode());
        // 1. 修改票据主表
        billCommonService.updateBillAndDetails(bill, billDetailList);
        // 修改扩展表
        updateSaleExtend(bill, billDto.getBillExtSale());
        return Result.ok();
    }

    /**
     * 关闭sale_order单据
     *
     * @param billId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result close(Long billId) throws CustomsException {
        if (billCountService.countRelationBillByBillId(billId) > 0) {
            return Result.fail("该单据已有转销售单，请先作废对应的销售单！");
        }
        Bill bill = billCommonService.getBillById(billId);
        if ("1".equals(bill.getIsClose())) {
            return Result.fail("该单据已经关闭！");
        }
        billCommonService.close(billId);
        return Result.ok("关闭成功");
    }

    /**
     * list_stock
     * 新增扩展表
     */
    private void addSaleExtend(Bill bill, BillExtSale billExtSale) {
        billExtSale.setBillId(bill.getId());
        billSaleExtendService.saveData(billExtSale);
        // 同步客户信息
        billSaleService.syncCustomerBySale(billExtSale, bill.getComegoId());
    }

    /**
     * 修改扩展表
     *
     * @param bill
     * @param billExtSale
     */
    private void updateSaleExtend(Bill bill, BillExtSale billExtSale) {
        billExtSale.setBillId(bill.getId());
        billSaleExtendService.updateData(billExtSale);
        // 同步客户信息
        billSaleService.syncCustomerBySale(billExtSale, bill.getComegoId());
    }
}
