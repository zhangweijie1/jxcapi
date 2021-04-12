package com.mrguo.service.impl.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.SaleDetailReportMapper;
import com.mrguo.dto.report.SaleDetailReportDto;
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
public class SaleDetailReportServiceImpl {

    @Autowired
    private SaleDetailReportMapper saleDetailReportMapper;

    public Result listSaleDetail(PageParams<SaleDetailReportDto> pageParams) {
        Page<SaleDetailReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleDetailReportMapper.listSaleDetail(page, data));
        return Result.ok(page);
    }
}
