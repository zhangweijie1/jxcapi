package com.mrguo.controller.basedata.goods;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.goods.GoodsUnitServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsSkuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 后台管理商品Controller
 *
 * @author mrguo
 */
@Api(tags = "商品SKU")
@RestController
@RequestMapping("/goodSku")
public class GoodSkuContrller {

    @Autowired
    private GoodsSkuServiceImpl goodsskuService;
    @Autowired
    private GoodsskuMapper goodsskuMapper;
    @Autowired
    private GoodsUnitServiceImpl goodsUnitService;

    @ApiOperation(value = "新增或保存商品SKU")
    @ApiPermission(value = "good:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody @Validated GoodSkuAllInfoVo goodSkuAllInfoVo) throws Exception {
        if (goodSkuAllInfoVo.getGoodsSku().getId() == null) {
            return goodsskuService.addData(goodSkuAllInfoVo);
        } else {
            return goodsskuService.updateData(goodSkuAllInfoVo);
        }
    }

    @ApiOperation(value = "删除商品SKU, by skuId")
    @ApiPermission(value = "good:del", pname = "删除")
    @PostMapping("/delete/{skuId}")
    public Result delete(@PathVariable Long skuId) throws Exception {
        return goodsskuService.delData(skuId);
    }

    /**
     * 查询开始
     */
    @ApiOperation(value = "获取sku编号")
    @PostMapping("/getSkuNo")
    public Result genGoodSkuCode() throws Exception {
        return goodsskuService.getGoodSkuCode();
    }

    @ApiOperation(value = "根据skuId和unitId，查询该商品某单位的价格")
    @PostMapping("/getPrice/{skuId}/{unitId}")
    public Result getPriceBySkuUnit(@PathVariable Long skuId, @PathVariable Long unitId) throws Exception {
        return goodsskuService.getPriceBySkuAndUnitId(skuId, unitId);
    }

    @ApiOperation(value = "根据skuId，查询该商品详情", notes = "包含库存,price等数据")
    @ApiPermission(value = "good:list", pname = "查询")
    @PostMapping("/getDetailData/{skuId}")
    public Result getSkuDetailById(@PathVariable(required = true) Long skuId) throws Exception {
        return Result.ok(goodsskuService.getSkuInfoById(skuId));
    }

    @ApiOperation(value = "根据条形码，获取该商品信息")
    @PostMapping("/getData/{barcode}")
    public Result getSkuByBarcode(@PathVariable(required = true) String barcode) throws Exception {
        return Result.ok(goodsskuService.getSkuByBarcode(barcode));
    }

    @ApiOperation(value = "根据skuId，查询计量单位")
    @PostMapping("/listUnitData/{skuId}")
    public Result getUnitListBySkuId(@PathVariable(required = true) Long skuId) throws Exception {
        return Result.ok(goodsUnitService.getUnitListBySkuId(skuId));
    }

    @ApiOperation(value = "查询多个商品某级别的折扣率， by skuIds and levelId")
    @PostMapping("/getDiscountBySkuIdsAndLevelId")
    public Result getDiscountBySkuIdsAndLevelId(@RequestBody Map<String, Object> data) throws Exception {
        List<String> skuIds = (List<String>) data.get("skuIds");
        Long levelId = Long.valueOf((String) data.get("levelId"));
        return goodsskuService.getDiscountBySkuIdsAndLevelId(skuIds, levelId);
    }

    @ApiOperation(value = "分页查询商品SKU")
    @ApiPermission(value = "good:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listData(@RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        return goodsskuService.listData(pageParams);
    }

    @ApiOperation(value = "分页查询商品，在某仓库的库存信息")
    @PostMapping("/listDataAndStockPage/{storeId}")
    public Result listDataAndStockByStore(@PathVariable Long storeId,
                                          @RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        return goodsskuService.selectContainerStockByStore(pageParams);
    }

    @ApiOperation(value = "分页查询商品包含更多信息，unit，price等")
    @PostMapping("/listDataMorePage")
    public Result listDataMore(@RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        return goodsskuService.listDataMore(pageParams);
    }

    @ApiOperation(value = "分页查询热卖商品")
    @PostMapping("/listHotSaleDataPage")
    public Result listHotSaleData(@RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        return goodsskuService.selectHotSaleData(pageParams);
    }

    @ApiOperation(value = "分页查询最小库存商品")
    @PostMapping("/listMinStockPage/{storeId}")
    public Result listMinStockByStore(@PathVariable Long storeId,
                                      @RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        Map<String, Object> data = pageParams.getData();
        data.put("storeId", storeId);
        return goodsskuService.selectContainerStockByStore(pageParams);
    }

    @ApiOperation(value = "分页查询最大库存商品")
    @PostMapping("/listMaxStockPage/{storeId}")
    public Result listMaxStockByStore(@PathVariable Long storeId,
                                      @RequestBody PageParams<GoodsSku> pageParams) throws Exception {
        Map<String, Object> data = pageParams.getData();
        data.put("storeId", storeId);
        return goodsskuService.selectMaxStockByStore(pageParams);
    }

    @ApiOperation(value = "查询某编号的商品数量")
    @PostMapping("/countByCode")
    public Result countByCode(@RequestBody String code) throws Exception {
        return Result.ok(goodsskuMapper.countByCode(code));
    }
}
