package com.mrguo.controller.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bill.Bill;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.bill.BillTrackingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 订单跟踪
 * @date 2020/6/410:18 AM
 * @updater 郭成兴
 * @updatedate 2020/6/410:18 AM
 */
@Api(tags = "订单跟踪",description="跟踪订单状况")
@RestController
@RequestMapping("/bill/tracking")
public class BillTrackingController {

    @Autowired
    private BillTrackingServiceImpl billTrackingService;

    @ApiOperation(value = "查询销售订单状况")
    @ApiPermission(value = "tracking:list", pname = "查询")
    @PostMapping("/listSale")
    public Result listSaleData(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return billTrackingService.getSaleListData(pageParams);
    }

    @ApiPermission(value = "tracking:export", pname = "导出")
    @PostMapping("/export")
    public IPage<Bill> export(@RequestBody PageParams<Bill> pageParams) throws Exception {
        return null;
    }
}
