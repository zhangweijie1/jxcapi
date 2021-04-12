package com.mrguo.controller.logs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.entity.log.LogGoodsCostPrice;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.log.LogGoodsCostpriceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 商品成本价明细Controller
 *
 * @author mrguo
 */
@RestController
@RequestMapping("/log/costPrice")
public class LogCostPriceController {

    @Autowired
    private LogGoodsCostpriceServiceImpl logGoodsCostpriceService;

    /**
     * 查询某商品的成本价明细
     *
     * @return
     * @throws Exception
     */
    @ApiPermission(value = "stock_query:list", pname = "查询")
    @PostMapping("/listCostDetail/{skuId}")
    public IPage<LogGoodsCostPrice> listDataBySkuId(@PathVariable Long skuId,
            @RequestBody PageParams<LogGoodsCostPrice> pageParams) throws Exception {
        Map<String, Object> data = pageParams.getData();
        data.put("skuId", skuId);
        return logGoodsCostpriceService.listDataBySkuId(pageParams);
    }
}
