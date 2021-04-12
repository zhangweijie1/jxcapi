package com.mrguo.controller.basedata.goods;

import com.mrguo.common.entity.Result;
import com.mrguo.service.impl.basedata.goods.GoodsSpuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理商品Controller
 *
 * @author mrguo
 */
@Api(tags = "商品SPU")
@RestController
@RequestMapping("/goodSpu")
public class GoodSpuContrller {

    @Autowired
    private GoodsSpuServiceImpl goodsspuService;

    @ApiOperation(value = "获取SPU编号")
    @PostMapping("/getSpuNo")
    public Result genGoodSpuCode() throws Exception {
        return goodsspuService.genGoodSpuCode();
    }
}
