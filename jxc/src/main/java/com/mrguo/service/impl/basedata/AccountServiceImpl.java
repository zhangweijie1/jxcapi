package com.mrguo.service.impl.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bsd.AccountMapper;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.service.inter.bill.BillCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName AccountServiceImpl
 * @Description 账户
 * @date 2019/12/175:08 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:08 PM
 */
@Service("accountServiceImpl")
public class AccountServiceImpl extends BaseServiceImpl<Account> {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<Account> getMapper() {
        return accountMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addOrUpdateData(Account account) {
        Long userId = (Long) request.getAttribute("userId");
        Date date = new Date();
        if (account.getId() == null) {
            // add
            account.setId(IDUtil.getSnowflakeId());
            account.setCreateTime(date);
            account.setUpdateTime(date);
            account.setCreateUserId(userId);
            account.setUpdateUserId(userId);
            return Result.ok(super.saveData(account));
        } else {
            if ("1".equals(account.getIsDefault())) {
                Account target = new Account();
                Example example = new Example(Account.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("isDefault", "1");
                target.setIsDefault("0");
                target.setUpdateUserId(userId);
                target.setUpdateTime(date);
                accountMapper.updateByExampleSelective(target, example);
            }
            return Result.ok(super.updateData(account));
        }
    }

    /**
     * 给某账户新增余额
     *
     * @param accountId
     * @param disAmount
     * @return
     */
    public Account addAccountRemainAmount(Long accountId, BigDecimal disAmount) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        BigDecimal targetAmount = account.getAmount().add(disAmount);
        account.setAmount(targetAmount);
        accountMapper.updateByPrimaryKeySelective(account);
        return account;
    }

    public Result delAccountData(Long accountId) throws CustomsException {
        if (billCountService.countByAccountId(accountId) > 0) {
            throw new CustomsException("该账户已有单据引用，不得删除！");
        }
        accountMapper.deleteByPrimaryKey(accountId);
        return Result.ok();
    }

    public Result listPage(PageParams<Account> pageParams) throws IOException {
        Page<Account> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(accountMapper.listPage(page, data));
        return Result.ok(page);
    }

    public List<Account> listOptions() {
        return accountMapper.listOptions();
    }

    public List<Account> getListDataByIds(List<Long> ids) {
        List<String> idsstr = ids.stream().map(String::valueOf).collect(Collectors.toList());
        return accountMapper.selectDataByIds(idsstr);
    }
}
