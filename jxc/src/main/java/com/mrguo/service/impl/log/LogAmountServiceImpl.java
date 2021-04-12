package com.mrguo.service.impl.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.log.LogAmountMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.entity.log.LogAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author 郭成兴
 * @ClassName LogAmountServiceImpl
 * @Description 账户资金流
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service("logAmountServiceImpl")
public class LogAmountServiceImpl extends BaseServiceImpl<LogAmount> {

    @Autowired
    private LogAmountMapper logAmountMapper;

    @Override
    public MyMapper<LogAmount> getMapper() {
        return logAmountMapper;
    }

    public void addDataByBill(Bill bill,
                              BigDecimal remainAmount,
                              String isCancleBill) throws CustomsException {
        valiBillData(bill);
        List<String> inCats = Arrays.asList("sale", "purchase_return");
        List<String> outCats = Arrays.asList("purchase", "sale_return");
        if (inCats.contains(bill.getBillCat())) {
            // 收入
            LogAmount logAmountByBill = getLogAmountByBill(bill,
                    "1", remainAmount, isCancleBill);
            logAmountMapper.insertSelective(logAmountByBill);
        }
        if (outCats.contains(bill.getBillCat())) {
            // 支出
            LogAmount logAmountByBill = getLogAmountByBill(bill,
                    "0", remainAmount, isCancleBill);
            logAmountMapper.insertSelective(logAmountByBill);
        }
    }

    public void addDataByFinBill(FinBill bill, BigDecimal remainAmount) throws CustomsException {
        _addDataByFinBill(bill, remainAmount, "0");
    }

    public void addDataByFinBill(FinBill bill, BigDecimal remainAmount, String isCancleBill) throws CustomsException {
        _addDataByFinBill(bill, remainAmount, isCancleBill);
    }

    private void _addDataByFinBill(FinBill bill, BigDecimal remainAmount, String isCancleBill) throws CustomsException {
        valiBillFinData(bill);
        // 收款单，日常收入
        List<String> receBillCats = Arrays.asList("fin_comego_in", "fin_dayily_in");
        // 付款单，日常支出
        List<String> payBillCats = Arrays.asList("fin_comego_out", "fin_dayily_out");
        if (receBillCats.contains(bill.getBillCat())) {
            LogAmount logAmountByBill = getLogAmountByFinBill(bill, "1", remainAmount, isCancleBill);
            logAmountMapper.insertSelective(logAmountByBill);
        }
        if (payBillCats.contains(bill.getBillCat())) {
            LogAmount logAmountByBill = getLogAmountByFinBill(bill, "0", remainAmount, isCancleBill);
            logAmountMapper.insertSelective(logAmountByBill);
        }
    }

    public Result listData(PageParams<Map<String, Object>> pageParams) {
        Page<Map<String, Object>> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(logAmountMapper.listCustom(page, data));
        return Result.ok(page);
    }

    private void valiBillData(Bill bill) throws CustomsException {
        if (bill.getAccountId() == null) {
            throw new CustomsException("请选择结算账户！");
        }
        if (bill.getAmountPaid() == null) {
            throw new CustomsException("实收(付)不能为空！");
        }
    }

    private void valiBillFinData(FinBill bill) throws CustomsException {
        if (bill.getAccountId() == null) {
            throw new CustomsException("请选择结算账户！");
        }
        if (bill.getAmountPaid() == null) {
            throw new CustomsException("实收(付)不能为空！");
        }
    }

    /**
     * @param bill
     * @param remainAmount 结余的金额
     * @return
     * @throws CustomsException
     */
    private LogAmount getLogAmountByBill(Bill bill,
                                         String cat,
                                         BigDecimal remainAmount,
                                         String isCancleBill) throws CustomsException {
        LogAmount logAmount = new LogAmount();
        logAmount.setId(IDUtil.getSnowflakeId());
        logAmount.setBillId(bill.getId());
        logAmount.setAccountId(bill.getAccountId());
        if ("1".equals(isCancleBill)) {
            logAmount.setAmount(bill.getAmountPaid().negate());
        } else {
            logAmount.setAmount(bill.getAmountPaid());
        }
        logAmount.setRemainAmount(remainAmount);
        logAmount.setCreateTime(new Date());
        logAmount.setComegoId(bill.getComegoId());
        logAmount.setCat(cat);
        logAmount.setIsCancleBill(isCancleBill);
        return logAmount;
    }

    private LogAmount getLogAmountByFinBill(FinBill bill,
                                            String cat,
                                            BigDecimal remainAmount,
                                            String isCancleBill) {
        LogAmount logAmount = new LogAmount();
        logAmount.setId(IDUtil.getSnowflakeId());
        logAmount.setBillId(bill.getId());
        logAmount.setAccountId(bill.getAccountId());
        if ("1".equals(isCancleBill)) {
            logAmount.setAmount(bill.getAmountPaid().negate());
        } else {
            logAmount.setAmount(bill.getAmountPaid());
        }
        logAmount.setRemainAmount(remainAmount);
        logAmount.setCreateTime(new Date());
        logAmount.setComegoId(bill.getComegoId());
        logAmount.setCat(cat);
        logAmount.setIsCancleBill(isCancleBill);
        return logAmount;
    }
}
