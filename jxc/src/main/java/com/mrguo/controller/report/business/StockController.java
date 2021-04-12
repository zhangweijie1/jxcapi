package com.mrguo.controller.report.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.vo.basedata.StockOfSkuVo;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.log.LogGoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 库存查询
 * @date 2019/12/305:19 PM
 * @updater 郭成兴
 * @updatedate 2019/12/305:19 PM
 */
@Api(tags = "库存查询")
@RestController
@RequestMapping("/stockQuery")
public class StockController {

    @Autowired
    private GoodsStockServiceImpl stockService;
    @Autowired
    private LogGoodsStockServiceImpl stockLogService;

    @ApiOperation(value = "获取库存列表", notes = "")
    @ApiPermission(value = "stock_query:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<StockOfSkuVo> pageParams) throws Exception {
        return stockService.getStockList(pageParams);
    }

    @ApiOperation(value = "查询商品库存（按仓库分组）)", notes = "显示不同仓库的库存信息")
    @ApiPermission(value = "stock_query:list", pname = "查询")
    @PostMapping("/listStocksGroupByStore/{skuId}")
    public Result getStockGroupStoreBySku(@PathVariable Long skuId) throws Exception {
        return Result.ok(stockService.getStockGroupStoreBySkuId(skuId));
    }

    @ApiOperation(value = "查询某商品库存 (合并不同仓库库存)", notes = "合并不同仓库库存")
    @ApiPermission(value = "stock_query:list", pname = "查询")
    @PostMapping("/getStockTotalOfStore/{skuId}")
    public Result getStockBySkuId(@PathVariable Long skuId) throws Exception {
        return Result.ok(stockService.getStockBySkuId(skuId));
    }

    @ApiOperation(value = "查询某商品的库存流水", notes = "")
    @ApiPermission(value = "stock_query:log", pname = "查询库存流水")
    @PostMapping("/listStockLogs/{skuId}")
    public IPage<LogGoodsStock> getStockLogBySkuId(@RequestBody PageParams<LogGoodsStock> pageParams) throws Exception {
        if (pageParams.getData().get("skuId") == null) {
            throw new CustomsException("商品ID不能为空！");
        }
        return stockLogService.getStockLogByGood(pageParams);
    }
}
