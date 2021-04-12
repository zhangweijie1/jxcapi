package com.mrguo.controller.basedata.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.service.impl.basedata.goods.GoodsStockWarnServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: GoodStockWarnController
 * @Description:
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/5 8:33 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@Api(tags = "商品库存预警")
@RestController
@RequestMapping("/goodStockWarn")
public class GoodStockWarnController {

    @Autowired
    private GoodsStockWarnServiceImpl goodsStockWarnService;

    @ApiOperation(value = "查询超出预警警戒线的商品数量", notes = "超过最大，低于最小")
    @PostMapping("/countStockWarn")
    public Result countStockWarn() throws Exception {
        return Result.ok(goodsStockWarnService.countStockWarn());
    }

    @ApiOperation(value = "查询超出预警警戒线的商品列表")
    @PostMapping("/listWarningGoodsPage")
    public IPage<Map<String, Object>> getWarningGoods(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return goodsStockWarnService.selectWarningGoods(pageParams);
    }
}
