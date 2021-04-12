package com.mrguo.service.impl.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.log.LogDebtMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.fin.FinBill;
import com.mrguo.entity.log.LogDebt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author 郭成兴
 * @ClassName LogDebtServiceImpl
 * @Description 应收应付欠款日志
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class LogDebtServiceImpl extends BaseServiceImpl<LogDebt> {

    @Autowired
    private LogDebtMapper logDebtMapper;

    @Override
    public MyMapper<LogDebt> getMapper() {
        return logDebtMapper;
    }

    /**
     * 新增应收应付欠款by - bill
     *
     * @param bill         单据，只提供单据即可
     * @param remainDebt   结余欠款
     * @param isCancleBill 是否作废单据
     */
    public void addDataByBill(Bill bill,
                              BigDecimal remainDebt,
                              String isCancleBill) throws CustomsException {
        valiBillData(bill);
        _addDataByBill(
                bill.getId(),
                bill.getBillCat(),
                bill.getComegoId(),
                bill.getAmountDebt(),
                remainDebt,
                isCancleBill);
    }

    /**
     * 新增应收应付欠款, 灵活度更高（新增的接口）
     *
     * @param billId       单据Id
     * @param comegoId     往来单位
     * @param cat          应收1，应付0
     * @param thisDebt     本次欠款
     * @param remainDebt   结余欠款
     * @param isCancleBill 是否是作废单据
     * @throws CustomsException
     */
    public void addDataByBill(
            Long billId,
            Long comegoId,
            String cat,
            BigDecimal thisDebt,
            BigDecimal remainDebt,
            String isCancleBill) throws CustomsException {
        //
        _addDataByBill(
                billId,
                comegoId,
                cat,
                thisDebt,
                remainDebt,
                isCancleBill);
    }

    /**
     * 作废单据的时候使用
     *
     * @param billId
     * @param comegoId
     * @param cat
     * @param thisDebt
     * @param remainDebt
     * @throws CustomsException
     */
    public void addCancleDataByBill(
            Long billId,
            Long comegoId,
            String cat,
            BigDecimal thisDebt,
            BigDecimal remainDebt) throws CustomsException {
        //
        LogDebt logDebt = getLogDebtByBill(billId, comegoId, "1", cat, thisDebt, remainDebt);
        logDebtMapper.insertSelective(logDebt);
    }


    public IPage<LogDebt> listData(PageParams<LogDebt> pageParams) {
        Page<LogDebt> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(logDebtMapper.listCustom(page, data));
        return page;
    }


    private void _addDataByBill(Long billId, String billCat, Long comegoId,
                                BigDecimal thisDebt,
                                BigDecimal remainDebt,
                                String isCancleBill) throws CustomsException {
        // 应收 1
        List<String> receCats = Arrays.asList("sale", "sale_return", "fin_comego_in");
        // 应付 0
        List<String> payCats = Arrays.asList("purchase", "purchase_return", "fin_comego_out");
        if (receCats.contains(billCat)) {
            // 应收(销售,销售退货，回收欠款)
            if (!BigDecimal.ZERO.equals(thisDebt)) {
                LogDebt logDebtByBill = getLogDebtByBill(
                        billId,
                        billCat,
                        thisDebt,
                        comegoId,
                        "1",
                        remainDebt, isCancleBill);
                logDebtMapper.insertSelective(logDebtByBill);
            }
        }
        if (payCats.contains(billCat)) {
            // 应付（进货，进货退货）
            if (!BigDecimal.ZERO.equals(thisDebt)) {
                LogDebt logDebtByBill = getLogDebtByBill(
                        billId,
                        billCat,
                        thisDebt,
                        comegoId,
                        "0",
                        remainDebt, isCancleBill);
                logDebtMapper.insertSelective(logDebtByBill);
            }
        }
    }

    private void _addDataByBill(Long billId, Long comegoId,
                                String cat,
                                BigDecimal thisDebt,
                                BigDecimal remainDebt,
                                String isCancleBill) throws CustomsException {
        // 应收 1， 应付 0
        // 应收(销售,销售退货，回收欠款)
        if (!BigDecimal.ZERO.equals(thisDebt)) {
            LogDebt logDebtByBill = getLogDebtByBill(
                    billId,
                    comegoId,
                    isCancleBill,
                    cat,
                    thisDebt,
                    remainDebt);
            logDebtMapper.insertSelective(logDebtByBill);
        }
    }

    private LogDebt getLogDebtByBill(Long billId,
                                     String billCat,
                                     BigDecimal thisDebt,
                                     Long comegoId,
                                     String cat,
                                     BigDecimal remainDebt,
                                     String isCancleBill) {
        LogDebt logDebt = new LogDebt();
        logDebt.setId(IDUtil.getSnowflakeId());
        logDebt.setBillId(billId);
        logDebt.setIsCancleBill(isCancleBill);
        logDebt.setCat(cat);
        List<String> returnCats = Arrays.asList("sale_return", "purchase_return");
        if (returnCats.contains(billCat)) {
            thisDebt = thisDebt.negate();
        }
        if ("1".equals(isCancleBill)) {
            logDebt.setAmount(thisDebt.negate());
        } else if ("0".equals(isCancleBill)) {
            logDebt.setAmount(thisDebt);
        } else {
            logDebt.setAmount(thisDebt);
        }
        logDebt.setComegoId(comegoId);
        logDebt.setCreateTime(new Date());
        logDebt.setRemainAmount(remainDebt);
        return logDebt;
    }

    private LogDebt getLogDebtByBill(Long billId,
                                     Long comegoId,
                                     String isCancleBill,
                                     String cat,
                                     BigDecimal thisDebt,
                                     BigDecimal remainDebt) {
        LogDebt logDebt = new LogDebt();
        Date date = new Date();
        logDebt.setId(IDUtil.getSnowflakeId());
        logDebt.setBillId(billId);
        logDebt.setIsCancleBill(isCancleBill);
        logDebt.setCat(cat);
        logDebt.setAmount(thisDebt);
        logDebt.setComegoId(comegoId);
        logDebt.setCreateTime(date);
        logDebt.setRemainAmount(remainDebt);
        return logDebt;
    }

    private void valiBillData(Bill bill) throws CustomsException {
        if (bill.getAmountDebt() == null) {
            throw new CustomsException("欠款金额不能为空！");
        }
    }
}
