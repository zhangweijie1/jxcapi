package com.mrguo.service.impl.fin;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.basedata.CustomerServiceImpl;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import com.mrguo.service.impl.log.LogDebtServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName FinComegoReceBillServiceImpl
 * @Description 收款单
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class FinComegoReceBillServiceImpl {

    @Autowired
    private FinBillServiceImpl finBillService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private LogAmountServiceImpl logAmountService;
    @Autowired
    private LogDebtServiceImpl logDebtService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private CustomerServiceImpl customerService;

    /**
     * 新增往来-收款单
     * 1. 新增单据
     * 2. 影响因素：账户余额，往来单位欠款
     *
     * @param finbill
     * @return
     */
    public Result addReceData(FinBill finbill) throws CustomsException {
        // 1. 新增单据
        finbill.setId(IDUtil.getSnowflakeId());
        finbill.setDirection("1");
        finbill.setBillCat(BillCatEnum.fin_comego_in.getCode());
        finbill.setBillCatName(BillCatEnum.fin_comego_in.getMessage());
        finbill.setCreateTime(new Date());
        finbill.setUpdateTime(finbill.getCreateTime());
        finBillService.saveData(finbill);
        // 2. 影响因素：账户余额，往来单位欠款
        // a. 账户金额(资金流水)
        BigDecimal amountPaid = finbill.getAmountPaid();
        Account account = accountService.addAccountRemainAmount(finbill.getAccountId(),
                amountPaid);
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByFinBill(finbill, remainAmount);
        // b. 往来单位欠款(客户欠款明细)
        BigDecimal thisDebt = amountPaid.negate();
        Customer customer = customerService.addDebt(finbill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addDataByBill(
                finbill.getId(),
                finbill.getComegoId(),
                "1",
                thisDebt,
                remainDebt, "0");
        return Result.ok();
    }

    /**
     * 作废单据
     * 回滚：账户余额，往来单位欠款
     *
     * @param billId
     */
    public Result cancleBill(Long billId) throws CustomsException {
        FinBill finBill = finBillService.selectByPrimaryKey(billId);
        if ("1".equals(finBill.getIsCancle())) {
            return Result.fail("该单据已作废，勿重复作废！");
        }
        // 1. 回滚账户金额
        BigDecimal amountPaid = finBill.getAmountPaid();
        Account account = accountService.addAccountRemainAmount(finBill.getAccountId(),
                amountPaid.negate());
        BigDecimal remainAmount = account.getOriginAmount().add(account.getAmount());
        logAmountService.addDataByFinBill(finBill, remainAmount, "1");
        // 2. 回滚往来单位欠款
        BigDecimal thisDebt = amountPaid;
        Customer customer = customerService.addDebt(finBill.getComegoId(), thisDebt);
        BigDecimal remainDebt = customer.getOriginDebt().add(customer.getDebt());
        logDebtService.addCancleDataByBill(
                finBill.getId(),
                finBill.getComegoId(),
                "1",
                thisDebt,
                remainDebt);
        // 单据本身is_cancle
        finBillService.cancleBill(finBill);
        return Result.ok();
    }

    public Result getBillCode() throws CustomsException {
        return Result.ok(billOrderNoService.getBillCode(BillCatEnum.fin_comego_in.getCode()));
    }
}
