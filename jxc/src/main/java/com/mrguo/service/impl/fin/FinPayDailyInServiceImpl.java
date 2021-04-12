package com.mrguo.service.impl.fin;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.basedata.CustomerServiceImpl;
import com.mrguo.service.impl.basedata.SupplierServiceImpl;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import com.mrguo.service.impl.log.LogDebtServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class FinPayDailyInServiceImpl {

    @Autowired
    private FinBillServiceImpl finBillService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private LogAmountServiceImpl logAmountService;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private SupplierServiceImpl supplierService;
    @Autowired
    private LogDebtServiceImpl logDebtService;

    /**
     * 日常-收入
     * 影响的元素：账户余额，往来单位欠款（如果有单位的话）
     *
     * @param bill
     * @return
     */
    public Result addDayilyInData(FinBill bill) throws CustomsException {
        // 1. 单据
        BillCatEnum billCat = BillCatEnum.fin_dayily_in;
        bill.setDirection("1");
        bill.setBillCat(billCat.getCode());
        bill.setBillCatName(billCat.getMessage());
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(), billCat.getCode()));
        finBillService.addData(bill);
        // 2. 影响的元素：账户余额，往来单位欠款（如果有单位的话）
        // a. 账户余额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(), bill.getAmountPaid());
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByFinBill(bill, remainAmount, "0");
        // b. 往来单位欠款 ( 判断是客户还是供货商 )
        if (bill.getComegoId() != null) {
            BigDecimal amountPayable = bill.getAmountPayable();
            BigDecimal amountPaid = bill.getAmountPaid();
            BigDecimal thisDebt = amountPayable.subtract(amountPaid);
            Customer c = customerService.selectByPrimaryKey(bill.getComegoId());
            if (c != null) {
                // 是客户,应收1
                Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
                BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
                logDebtService.addDataByBill(
                        bill.getId(),
                        bill.getComegoId(),
                        "1",
                        thisDebt,
                        remainDebt, "0");
            } else {
                Supplier s = supplierService.selectByPrimaryKey(bill.getComegoId());
                if (s != null) {
                    // 供应商 应付0
                    Supplier supplier = supplierService.addDebt(bill.getComegoId(), thisDebt.negate());
                    BigDecimal remainDebt = supplier.getOriginDebt().add(supplier.getDebt());
                    logDebtService.addDataByBill(
                            bill.getId(),
                            bill.getComegoId(),
                            "0",
                            thisDebt.negate(),
                            remainDebt, "0");
                } else {
                    throw new CustomsException("该往来单位不存在！");
                }
            }
        }
        return Result.ok();
    }

    /**
     * 作废单据
     * 影响的元素：账户余额，往来单位欠款（如果有单位的话）
     *
     * @param billId
     * @return
     */
    public Result cancleBill(Long billId) throws CustomsException {
        FinBill bill = finBillService.selectByPrimaryKey(billId);
        if ("1".equals(bill.getIsCancle())) {
            return Result.fail("该单据已作废，无需重复作废！");
        }
        // 1. 账户余额
        Account account = accountService.addAccountRemainAmount(bill.getAccountId(), bill.getAmountPaid().negate());
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByFinBill(bill, remainAmount, "1");
        // 2. 往来单位欠款
        if (bill.getComegoId() != null) {
            BigDecimal amountPayable = bill.getAmountPayable();
            BigDecimal amountPaid = bill.getAmountPaid();
            BigDecimal thisDebt = amountPayable.subtract(amountPaid).negate();
            Customer c = customerService.selectByPrimaryKey(bill.getComegoId());
            if (c != null) {
                // 是客户,应收1
                Customer customer = customerService.addDebt(bill.getComegoId(), thisDebt);
                BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
                logDebtService.addDataByBill(
                        bill.getId(),
                        bill.getComegoId(),
                        "1",
                        thisDebt,
                        remainDebt, "1");
            } else {
                Supplier s = supplierService.selectByPrimaryKey(bill.getComegoId());
                if (s != null) {
                    // 供应商 应付0
                    Supplier supplier = supplierService.addDebt(bill.getComegoId(), thisDebt.negate());
                    BigDecimal remainDebt = supplier.getOriginDebt().add(supplier.getDebt());
                    logDebtService.addDataByBill(
                            bill.getId(),
                            bill.getComegoId(),
                            "0",
                            thisDebt.negate(),
                            remainDebt, "1");
                } else {
                    throw new CustomsException("该往来单位不存在！");
                }
            }
        }
        // 单据本身is_cancle
        finBillService.cancleBill(bill);
        return Result.ok();
    }

    public Result getBillCode() throws CustomsException {
        return Result.ok(billOrderNoService.getBillCode(BillCatEnum.fin_dayily_in.getCode()));
    }

    public Result listDayilyInData(PageParams<FinBill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.fin_dayily_in.getCode());
        return Result.ok(finBillService.listData(pageParams));
    }
}
