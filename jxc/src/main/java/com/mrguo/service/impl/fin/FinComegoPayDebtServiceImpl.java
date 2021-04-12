package com.mrguo.service.impl.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.fin.FinComegoPayDebtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName FinComegoPayDebtServiceImpl
 * @Description 应付欠款
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class FinComegoPayDebtServiceImpl {

    @Autowired
    private FinComegoPayDebtMapper finComegoPayDebtMapper;

    public Result listDataGroupByComegoPage(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
        page.setRecords(finComegoPayDebtMapper.listDataGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listDebtDetailByComegoId(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
        page.setRecords(finComegoPayDebtMapper.listDebtDetailByComegoId(page, data));
        return Result.ok(page);
    }

    public Result getStatistics(Map<String, Object> data) {
        return Result.ok(finComegoPayDebtMapper.selectStatistics(data));
    }
}
