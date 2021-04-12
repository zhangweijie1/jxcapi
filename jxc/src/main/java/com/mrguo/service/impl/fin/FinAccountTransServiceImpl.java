package com.mrguo.service.impl.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.fin.FinAccountTransMapper;
import com.mrguo.entity.bsd.Account;
import com.mrguo.dto.LogAmountAndAccountListDto;
import com.mrguo.entity.fin.FinAccountTrans;
import com.mrguo.entity.log.LogAmount;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/3/218:00 AM
 * @updater 郭成兴
 * @updatedate 2020/3/218:00 AM
 */
@Service("finAccountTransServiceImpl")
public class FinAccountTransServiceImpl extends BaseServiceImpl<FinAccountTrans> {

    @Autowired
    private FinAccountTransMapper finAccountTransMapper;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private LogAmountServiceImpl logAmountService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<FinAccountTrans> getMapper() {
        return finAccountTransMapper;
    }

    /**
     * 新增转账记录
     * 1. 转账单据
     * 2. 账户金额
     * 2. log日志
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(FinAccountTrans entity) {
        Date date = new Date();
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        // 1. 新增转账单据
        entity.setId(IDUtil.getSnowflakeId());
        entity.setCreateTime(date);
        entity.setBusinessTime(date);
        entity.setUpdateTime(date);
        super.saveData(entity);
        // 2. 账户金额
        List<Long> ids = Arrays.asList(entity.getAccountIn(), entity.getAccountOut());
        List<Account> accountList = accountService.getListDataByIds(ids);
        // 获取资金数据
        LogAmountAndAccountListDto logAmountAndAccountList = getLogAmountAndAccountList(
                entity,
                accountList, "0");
        accountList = logAmountAndAccountList.getAccountList();
        accountService.batchUpdateByPrimaryKeySelective(accountList);
        // 3 资金log
        List<LogAmount> logAmountList = logAmountAndAccountList.getLogAmountList();
        logAmountService.insertListData(logAmountList);
        return Result.ok();
    }

    public Result listPage(PageParams<FinAccountTrans> pageParams) {
        Page<FinAccountTrans> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(finAccountTransMapper.listCustom(page, data));
        return Result.ok(page);
    }

    public FinAccountTrans getDataById(Long id) {
        return finAccountTransMapper.selectOneById(id);
    }

    /**
     * 作废操作
     * 1. 转账单据
     * 2. 账户金额
     * 2. log日志
     *
     * @return
     */
    public Result cancelTransBill(Long billId) {
        // 1. 单据本身
        FinAccountTrans finAccountTrans = finAccountTransMapper.selectByPrimaryKey(billId);
        finAccountTrans.setIsCancle("1");
        finAccountTransMapper.updateByPrimaryKeySelective(finAccountTrans);
        // 2. 账户金额
        List<Long> ids = Arrays.asList(finAccountTrans.getAccountIn(), finAccountTrans.getAccountOut());
        List<Account> accountList = accountService.getListDataByIds(ids);
        // 获取资金数据
        LogAmountAndAccountListDto logAmountAndAccountList = getLogAmountAndAccountList(
                finAccountTrans,
                accountList, "1");
        accountList = logAmountAndAccountList.getAccountList();
        accountService.batchUpdateByPrimaryKeySelective(accountList);
        // 3 资金log
        List<LogAmount> logAmountList = logAmountAndAccountList.getLogAmountList();
        logAmountService.insertListData(logAmountList);
        return Result.ok();
    }

    private LogAmountAndAccountListDto getLogAmountAndAccountList(FinAccountTrans accountTrans,
                                                                  List<Account> accountList,
                                                                  String isCancleBill) {
        BigDecimal amount = accountTrans.getAmount();
        BigDecimal procedureAmount = accountTrans.getProcedureAmount();
        BigDecimal targetAmountIn = null;
        BigDecimal targetAmountOut = null;
        if ("0".equals(accountTrans.getProcedureUser())) {
            // 转出方付手续
            targetAmountOut = amount.add(procedureAmount);
            targetAmountIn = amount;
        } else {
            targetAmountOut = amount;
            targetAmountIn = amount.subtract(procedureAmount);
        }
        ArrayList<LogAmount> logAmounts = new ArrayList<>();
        Date date = new Date();
        if ("1".equals(isCancleBill)) {
            targetAmountIn = targetAmountIn.negate();
            targetAmountOut = targetAmountOut.negate();
        }
        for (Account account : accountList) {
            LogAmount logAmount = new LogAmount();
            if (account.getId().equals(accountTrans.getAccountIn())) {
                account.setAmount(account.getAmount().add(targetAmountIn));
                // 入
                logAmount.setAmount(targetAmountIn);
                logAmount.setCat("1");
            }
            if (account.getId().equals(accountTrans.getAccountOut())) {
                account.setAmount(account.getAmount().subtract(targetAmountOut));
                // 出
                logAmount.setAmount(targetAmountOut);
                logAmount.setCat("0");
            }
            logAmount.setId(IDUtil.getSnowflakeId());
            logAmount.setBillId(accountTrans.getId());
            logAmount.setAccountId(account.getId());
            logAmount.setCreateTime(date);
            BigDecimal thisAmount = account.getAmount();
            if ("1".equals(isCancleBill)) {
                thisAmount = account.getAmount().negate();
            }
            BigDecimal remainAmount = account.getOriginAmount().add(thisAmount);
            logAmount.setRemainAmount(remainAmount);
            logAmount.setIsCancleBill(isCancleBill);
            logAmounts.add(logAmount);
        }
        LogAmountAndAccountListDto logAmountAndAccountListDto = new LogAmountAndAccountListDto();
        logAmountAndAccountListDto.setAccountList(accountList);
        logAmountAndAccountListDto.setLogAmountList(logAmounts);
        return logAmountAndAccountListDto;
    }
}
