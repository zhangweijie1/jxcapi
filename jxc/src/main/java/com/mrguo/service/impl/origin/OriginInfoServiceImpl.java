package com.mrguo.service.impl.origin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.origin.OriginMasterMapper;
import com.mrguo.dao.setup.OriginMapper;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.dto.setup.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/152:05 PM
 * @updater 郭成兴
 * @updatedate 2020/6/152:05 PM
 */
@Service
public class OriginInfoServiceImpl {

    @Autowired
    private OriginMapper originMapper;
    @Autowired
    private OriginMasterMapper originMasterMapper;


    public Result listStock(PageParams<Origin> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Origin> page = pageParams.getPage();
        page.setRecords(originMapper.listStock(page, data));
        return Result.ok(page);
    }

    public Result listDeptRece(PageParams<Customer> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Customer> page = pageParams.getPage();
        page.setRecords(originMapper.listDeptRece(page, data));
        return Result.ok(page);
    }

    public Result listDeptPay(PageParams<Supplier> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Supplier> page = pageParams.getPage();
        page.setRecords(originMapper.listDeptPay(page, data));
        return Result.ok(page);
    }

    public Result listAccountOrigin(PageParams<Account> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Account> page = pageParams.getPage();
        page.setRecords(originMapper.listAccountOrigin(page, data));
        return Result.ok(page);
    }
}
