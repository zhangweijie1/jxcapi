package com.mrguo.service.impl.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.bill.BillTrackingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName BillTrackingServiceImpl
 * @Description 订单跟踪
 * @date 2020/7/88:29 AM
 * @updater 郭成兴
 * @updatedate 2020/7/88:29 AM
 */
@Service
public class BillTrackingServiceImpl {

    @Autowired
    private BillTrackingMapper billTrackingMapper;

    public Result getSaleListData(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
        List<Map<String, Object>> result = billTrackingMapper.selectSaleTrack(page, data);
        page.setRecords(result);
        return Result.ok(page);
    }
}
