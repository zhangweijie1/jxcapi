package com.mrguo.service.impl.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.PurchaseReportMapper;
import com.mrguo.dto.report.PurchaseReportDto;
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
public class PurchaseReportServiceImpl {

    @Autowired
    private PurchaseReportMapper purchaseReportMapper;

    public Result listDataGroupByBillPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listDataGroupByBillPage(page, data));
        return Result.ok(page);
    }

    public Result listDataGroupByComegoPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String,Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listDataGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listDataGroupByGoodPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String,Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listDataGroupByGoodPage(page, data));
        return Result.ok(page);
    }

    public Result listGoodDetailBySkuIdGroupByBillPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listGoodDetailBySkuIdGroupByBillPage(page, data));
        return Result.ok(page);
    }

    public Result listGoodDetailBySkuIdGroupByComegoPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listGoodDetailBySkuIdGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listComegoDetailByComegoIdGroupByGoodPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listComegoDetailByComegoIdGroupByGoodPage(page, data));
        return Result.ok(page);
    }

    public Result listComegoDetailByComegoIdGroupByBillPage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listComegoDetailByComegoIdGroupByBillPage(page, data));
        return Result.ok(page);
    }

    /**
     * 某天的bill
     *
     * @param pageParams
     * @return
     */
    public Result listBillDetailByDatePage(PageParams<PurchaseReportDto> pageParams) {
        Page<PurchaseReportDto> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(purchaseReportMapper.listBillDetailByDatePage(page, data));
        return Result.ok(page);
    }
}
