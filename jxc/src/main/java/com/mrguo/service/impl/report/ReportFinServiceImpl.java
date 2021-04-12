package com.mrguo.service.impl.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.report.ReportFinMapper;
import com.mrguo.dto.report.ProfitPay;
import com.mrguo.dto.report.ProfitRece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/1710:40 AM
 * @updater 郭成兴
 * @updatedate 2020/7/1710:40 AM
 */

@Service
public class ReportFinServiceImpl {

    @Autowired
    private ReportFinMapper reportFinMapper;

    public Result getReceAmount(Map<String, Object> data) throws IllegalAccessException {
        ProfitRece profitRece = reportFinMapper.selectRece(data);
        Class<ProfitRece> c = ProfitRece.class;
        Field[] declaredFields = c.getDeclaredFields();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            String name = getReceNameByCode(fieldName);
            if (name != null) {
                Map<String, Object> option = new HashMap<>();
                field.setAccessible(true);
                option.put("code", fieldName);
                option.put("name", name);
                option.put("value", field.get(profitRece));
                result.add(option);
            }
        }
        return Result.ok(result);
    }

    public Result getPayAmount(Map<String, Object> data) throws IllegalAccessException {
        ProfitPay profitPay = reportFinMapper.selectPay(data);
        Class<ProfitPay> c = ProfitPay.class;
        Field[] declaredFields = c.getDeclaredFields();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (Field field : declaredFields) {
            Map<String, Object> option = new HashMap<>();
            field.setAccessible(true);
            String fieldName = field.getName();
            option.put("code", fieldName);
            option.put("name", getPayNameByCode(fieldName));
            if (profitPay == null) {
                option.put("value", BigDecimal.ZERO);
            } else {
                option.put("value", field.get(profitPay));
            }
            result.add(option);
        }
        return Result.ok(result);
    }

    public Result listPerformanceGroupByUser(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
        page.setRecords(reportFinMapper.selectPerformanceGroupByHandUserId(page, data));
        return Result.ok(page);
    }

    public Result getPerformanceInfo(Map<String, Object> data) {
        Map<String, Object> countSaleBill = reportFinMapper.selectCountSaleBill(data);
        Map<String, Object> maxSaleHandUser = reportFinMapper.selectMaxSaleHandUser(data);
        if (maxSaleHandUser != null) {
            countSaleBill.putAll(maxSaleHandUser);
        }
        return Result.ok(countSaleBill);
    }

    private String getReceNameByCode(String code) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("saleProfit", "销售毛利");
        maps.put("saleOther", "销售其他费用");
        maps.put("saleTax", "销售税额");
        maps.put("dailyIn", "日常收入");
        maps.put("inventory", "盘盈");
        maps.put("payDiscount", "付款优惠");
        maps.put("purchaseReturnOther", "销售退货其他费用");
        return maps.get(code);
    }

    private String getPayNameByCode(String code) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("dailyOut", "日常支出");
        maps.put("inventory", "盘亏");
        maps.put("purchaseOther", "进货其他费用");
        maps.put("tax", "进货税额");
        maps.put("receDiscount", "收款优惠");
        maps.put("transProcedure", "转账手续费");
        maps.put("stockOutFreight", "出库运费");
        maps.put("saleReturnOther", "销售退货其他费用");
        maps.put("purchaseReturnDis", "进货退货差价");
        maps.put("dispatchOther", "调拨其他费用");
        return maps.get(code);
    }
}
