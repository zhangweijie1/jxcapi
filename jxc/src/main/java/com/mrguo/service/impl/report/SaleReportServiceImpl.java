package com.mrguo.service.impl.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.SaleReportMapper;
import com.mrguo.dto.report.SaleReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author 郭成兴
 * @ClassName SaleReportServiceImpl
 * @Description
 * @date 2020/5/189:08 PM
 * @updater 郭成兴
 * @updatedate 2020/5/189:08 PM
 */
@Service
public class SaleReportServiceImpl {

    @Autowired
    private SaleReportMapper saleReportMapper;

    public Result listDataGroupByBillPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listDataGroupByBillPage(page, data));
        return Result.ok(page);
    }

    public Result listDataGroupByComegoPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String,Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listDataGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listDataGroupByGoodPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String,Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listDataGroupByGoodPage(page, data));
        return Result.ok(page);
    }

    public Result listGoodDetailBySkuIdGroupByBillPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        page.setRecords(saleReportMapper.listGoodDetailBySkuIdGroupByBillPage(page, pageParams.getData()));
        return Result.ok(page);
    }

    public Result listGoodDetailBySkuIdGroupByComegoPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listGoodDetailBySkuIdGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listComegoDetailByComegoIdGroupByGoodPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listComegoDetailByComegoIdGroupByGoodPage(page, data));
        return Result.ok(page);
    }

    public Result listComegoDetailByComegoIdGroupByBillPage(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listComegoDetailByComegoIdGroupByBillPage(page, data));
        return Result.ok(page);
    }

    /**
     * 某天的bill
     *
     * @param pageParams
     * @return
     */
    public Result listBillDetailByDate(PageParams<SaleReportDto> pageParams) {
        Page<SaleReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(saleReportMapper.listBillDetailByDate(page, data));
        return Result.ok(page);
    }
}
