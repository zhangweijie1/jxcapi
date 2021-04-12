package com.mrguo.service.impl.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.PurchaseSaleCompareReportMapper;
import com.mrguo.dto.report.PurchaseSaleCompareReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/189:08 PM
 * @updater 郭成兴
 * @updatedate 2020/5/189:08 PM
 */
@Service
public class PurchaseSaleCompareReportServiceImpl {

    @Autowired
    private PurchaseSaleCompareReportMapper purchaseSaleCompareReportMapper;

    public Result listCompare(PageParams<PurchaseSaleCompareReportDto> pageParams) {
        Page<PurchaseSaleCompareReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseSaleCompareReportMapper.listCompare(page, data));
        return Result.ok(page);
    }
}
