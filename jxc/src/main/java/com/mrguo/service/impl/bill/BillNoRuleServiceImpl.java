package com.mrguo.service.impl.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.sys.SysBillNoRuleMapper;
import com.mrguo.entity.sys.SysBillNoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 周健
 * @Date: 2020/1/16 10:40
 */
@Service
public class BillNoRuleServiceImpl extends BaseServiceImpl<SysBillNoRule> {
    @Autowired
    private SysBillNoRuleMapper sysBillNoRuleMapper;

    @Override
    public MyMapper<SysBillNoRule> getMapper() {
        return sysBillNoRuleMapper;
    }

    public Result addData(SysBillNoRule e) {
        return Result.ok(sysBillNoRuleMapper.insertSelective(e));
    }

    public Page<SysBillNoRule> listPage(PageParams<SysBillNoRule> pageParams) {
        Page<SysBillNoRule> page = pageParams.getPage();
        page.setRecords(sysBillNoRuleMapper.selectAllPage(page));
        return page;
    }

    public Result getDataById(Long id) {
        return Result.ok(sysBillNoRuleMapper.selectById(id));
    }
}
