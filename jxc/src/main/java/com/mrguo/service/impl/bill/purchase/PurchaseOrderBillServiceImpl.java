package com.mrguo.service.impl.bill.purchase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.service.inter.bill.BillAuditService;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 进货票据相关
 * @date 2020/1/187:03 PM
 * @updater 郭成兴
 * @updatedate 2020/1/187:03 PM
 */
@Service
public class PurchaseOrderBillServiceImpl implements BillAuditService,
        BillBaseService<Bill> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    @Qualifier("billAuditServiceImpl")
    private BillAuditService billAuditService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 新增采购订单（不需要管库存）
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillAndDetailsVo<Bill> billAndDetailsVo) throws CustomsException {
        Bill bill = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        billDetailList.forEach(item -> {
            item.setChangeQuantity(BigDecimal.ZERO);
            item.setChangeQuantity2(BigDecimal.ZERO);
        });
        // 处理数据
        BillCatEnum purchaseOrder = BillCatEnum.purchase_order;
        try {
            bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                    purchaseOrder.getCode()));
        } catch (CustomsException e) {
            return Result.fail(e.getMessage());
        }
        bill.setDirection("1");
        bill.setBillCat(purchaseOrder.getCode());
        bill.setBillCatName(purchaseOrder.getMessage());
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        // 新增
        billCommonService.addData(bill, billDetailList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancle(Long billId) throws CustomsException {
        if (billCountService.countRelationBillByBillId(billId) > 0) {
            throw new CustomsException("该单据已有转进货单，请先作废对应的进货单！");
        }
        Bill bill = billCommonService.getBillById(billId);
        if ("1".equals(bill.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
        billCommonService.cancle(billId);
        return Result.ok();
    }

    @Override
    public Result listPage(PageParams<Bill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Bill> page = pageParams.getPage();
        data.put("billCat", BillCatEnum.purchase_order.getCode());
        IPage<Bill> billIPage = billCommonService.listPage(page, data);
        return Result.ok(billIPage);
    }

    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.purchase_order));
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        TempPrintVo purchase = tempPrintConfig.getPurchaseOrder();
        BillAndDetailsVo<Bill> billBillAndDetailsVo = billCommonService.getBillAndDetailsById(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDefaultTempPrint(purchase, billBillAndDetailsVo);
        return Result.ok(defaultTempPrint);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result updateData(BillAndDetailsVo<Bill> billAndDetailsVo) {
        Bill bill = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        bill.setBillCat(BillCatEnum.purchase_order.getCode());
        try {
            billCommonService.updateBillAndDetails(bill, billDetailList);
            return Result.ok();
        } catch (CustomsException e) {
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result passAuditList(List<Long> billIds) {
        return billAuditService.passAuditList(billIds);
    }

    @Override
    public Result antiAuditList(List<Long> billIds) {
        return billAuditService.antiAuditList(billIds);
    }


    public BillAndDetailsVo<Bill> getPurchaseDataById(Long billId) throws IOException {
        return billCommonService.getBillAndDetailsById(billId);
    }

    public List<BillDetail> getNotTransDetailById(Long billId) {
        return billDetailService.listWaiteTransDataByBillId(billId);
    }

    public List<BillDetail> getHasTransDataById(Long billId) {
        return billDetailService.listHasTransDataByBillId(billId);
    }
}
