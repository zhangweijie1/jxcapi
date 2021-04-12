package com.mrguo.service.impl.basedata.goods.utils;

import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dto.goods.GoodAssemblyDto;
import com.mrguo.dto.goods.SkuInfoDto;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import com.mrguo.entity.goods.*;
import com.mrguo.entity.origin.OriginSkuCostprice;
import com.mrguo.entity.origin.OriginSkuStock;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName GoodsAddDataUtils
 * @Description 新增组合商品的工具类
 * @date 2020/7/299:53 AM
 * @updater 郭成兴
 * @updatedate 2020/7/299:53 AM
 */
@Service
public class GoodsAddDataUtils {

    private final static int DIGIT = 4;

    private BillOrderNoServiceImpl billOrderNoService;

    public GoodsAddDataUtils(BillOrderNoServiceImpl billOrderNoService) {
        this.billOrderNoService = billOrderNoService;
    }

    /**
     * 拆分组合商品（spu + skuList）
     * 拆分为spu + priceList + unitList + ...
     *
     * @param goodsAssemblyVo 组合数据
     * @return GoodAssemblyDto
     * @throws CustomsException
     */
    public GoodAssemblyDto splitAssemblyGood(GoodsAssemblyVo goodsAssemblyVo) throws CustomsException {
        // 1. 获取值
        Date date = new Date();
        // 2. 组装
        GoodAssemblyDto goodAssemblyDto = new GoodAssemblyDto();
        // 1. SPU
        GoodsSpu goodsSpu = goodsAssemblyVo.getGoodsSpu();
        initSpuData(goodsSpu);
        // SKU以及其他属性
        List<GoodSkuAllInfoVo> goodSkuAllInfoVoList = goodsAssemblyVo.getGoodSkuAllInfoVoList();
        List<GoodsSku> goodsSkuList = new ArrayList<>();
        List<GoodsPrice> goodsPriceList = new ArrayList<>();
        List<GoodsLevel> goodsLevelList = new ArrayList<>();
        List<GoodsUnit> goodsUnitList = new ArrayList<>();
        List<GoodsCostPrice> goodsCostPriceList = new ArrayList<>();
        List<GoodsBarcode> goodsBarcodeList = new ArrayList<>();
        List<GoodsStock> goodsStockList = new ArrayList<>();
        List<GoodsStockWarn> goodsStockWarnList = new ArrayList<>();
        List<OriginSkuStock> originSkuStockList = new ArrayList<>();
        List<OriginSkuCostprice> originSkuCostpriceList = null;
        List<GoodsCostPriceOrigin> originCurrentCostpriceList = null;
        for (GoodSkuAllInfoVo goodSkuAllInfoVo : goodSkuAllInfoVoList) {
            // sku
            GoodsSku goodssku = goodSkuAllInfoVo.getGoodsSku();
            initSkuData(goodsSpu, goodSkuAllInfoVo.getGoodsSku());
            goodsSkuList.add(goodssku);
            // unit
            for (GoodsUnit goodsUnit : goodSkuAllInfoVo.getGoodsUnitList()) {
                goodsUnit.setSkuId(goodssku.getId());
                goodsUnitList.add(goodsUnit);
            }
            // price
            for (GoodsPrice goodsprice : goodSkuAllInfoVo.getGoodsPriceList()) {
                goodsprice.setSkuId(goodssku.getId());
                goodsPriceList.add(goodsprice);
            }
            // level
            for (GoodsLevel goodsLevel : goodSkuAllInfoVo.getGoodsLevelList()) {
                goodsLevel.setSkuId(goodssku.getId());
                goodsLevelList.add(goodsLevel);
            }
            // 当前成本价
            GoodsCostPrice avgCostPrice = getAvgCostPrice(goodssku, goodSkuAllInfoVo.getGoodsStockList());
            goodsCostPriceList.add(avgCostPrice);
            // 期初成本价（结存表）
            originSkuCostpriceList = stockList2originCostList(goodssku, goodSkuAllInfoVo.getGoodsStockList());
            // 期初成本价（当前）
            originCurrentCostpriceList = origin2CurrentOrigin(originSkuCostpriceList);
            // 条形码barcode
            for (GoodsBarcode goodsBarcode : goodSkuAllInfoVo.getGoodsBarcodeList()) {
                goodsBarcode.setSkuId(goodssku.getId());
                goodsBarcodeList.add(goodsBarcode);
            }
            // stock
            List<SkuInfoDto> skuInfoDtoList = goodSkuAllInfoVo.getGoodsStockList();
            for (GoodsStock goodsStock : skuInfoDtoList) {
                initGoodsStock(goodsStock, goodssku.getId());
                goodsStockList.add(goodsStock);
            }
            // goodsStockWarn
            for (GoodsStockWarn goodsStockWarn : goodSkuAllInfoVo.getGoodsStockWarnList()) {
                goodsStockWarn.setSkuId(goodssku.getId());
                goodsStockWarnList.add(goodsStockWarn);
            }
            // 期初信息（库存，成本价）
            for (SkuInfoDto skuInfoDto : goodSkuAllInfoVo.getGoodsStockList()) {
                OriginSkuStock originSkuStock = getOriginSkuStock(goodssku.getId(), skuInfoDto);
                originSkuStockList.add(originSkuStock);
            }
        }
        goodAssemblyDto.setGoodsSpu(goodsSpu);
        goodAssemblyDto.setGoodsSkuList(goodsSkuList);
        goodAssemblyDto.setGoodsUnitList(goodsUnitList);
        goodAssemblyDto.setGoodsPriceList(goodsPriceList);
        goodAssemblyDto.setGoodsLevelList(goodsLevelList);
        goodAssemblyDto.setGoodsBarcodeList(goodsBarcodeList);
        goodAssemblyDto.setGoodsCostPriceList(goodsCostPriceList);
        goodAssemblyDto.setGoodsStockList(goodsStockList);
        goodAssemblyDto.setGoodsStockWarnList(goodsStockWarnList);
        goodAssemblyDto.setOriginSkuStockList(originSkuStockList);
        goodAssemblyDto.setOriginSkuCostpriceList(originSkuCostpriceList);
        goodAssemblyDto.setGoodsCostPriceOriginList(originCurrentCostpriceList);
        if (goodsSkuList.size() > 1) {
            goodsSpu.setIsEnableSpecs("1");
        } else {
            goodsSpu.setIsEnableSpecs("0");
        }
        return goodAssemblyDto;
    }

    /**
     * 获取sku的平均成本价（多仓库平均）
     *
     * @param goodssku       sku本身信息
     * @param skuInfoDtoList 包含sku详情信息
     * @return
     */
    private GoodsCostPrice getAvgCostPrice(GoodsSku goodssku,
                                           List<SkuInfoDto> skuInfoDtoList) {
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal totalQty = new BigDecimal("0");
        for (SkuInfoDto stock : skuInfoDtoList) {
            totalAmount = totalAmount.add(stock.getOriginCostPrice().multiply(stock.getOriginQuantity()));
            totalQty = totalQty.add(stock.getOriginQuantity());
        }
        BigDecimal targetCostPrice = totalQty.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : totalAmount.divide(totalQty, DIGIT, BigDecimal.ROUND_HALF_UP);
        GoodsCostPrice goodsCostPrice = new GoodsCostPrice();
        goodsCostPrice.setSkuId(goodssku.getId());
        goodsCostPrice.setPriceCost(targetCostPrice);
        return goodsCostPrice;
    }

    private List<OriginSkuCostprice> stockList2originCostList(GoodsSku goodssku,
                                                              List<SkuInfoDto> stockList) {
        Date date = new Date();
        ArrayList<OriginSkuCostprice> goodsCostPrices = new ArrayList<>();
        for (SkuInfoDto stock : stockList) {
            OriginSkuCostprice originSkuCostprice = new OriginSkuCostprice();
            originSkuCostprice.setId(IDUtil.getSnowflakeId());
            originSkuCostprice.setSkuId(goodssku.getId());
            originSkuCostprice.setStoreId(stock.getStoreId());
            originSkuCostprice.setCostprice(stock.getOriginCostPrice());
            originSkuCostprice.setBalanceTime(date);
            originSkuCostprice.setParentId(0L);
            goodsCostPrices.add(originSkuCostprice);
        }
        return goodsCostPrices;
    }

    /**
     * 结存表的期初 -> 当期表的期初
     *
     * @return
     */
    private List<GoodsCostPriceOrigin> origin2CurrentOrigin(List<OriginSkuCostprice> originSkuCostpriceList) {
        ArrayList<GoodsCostPriceOrigin> costPriceOrigins = new ArrayList<>();
        for (OriginSkuCostprice costprice : originSkuCostpriceList) {
            GoodsCostPriceOrigin costPriceOrigin = new GoodsCostPriceOrigin();
            costPriceOrigin.setSkuId(costprice.getSkuId());
            costPriceOrigin.setPriceCost(costprice.getCostprice());
            costPriceOrigin.setStoreId(costprice.getStoreId());
            costPriceOrigins.add(costPriceOrigin);
        }
        return costPriceOrigins;
    }

    /**
     * 获取商品期初库存
     *
     * @param skuId
     * @param skuInfoDto
     * @return
     */
    private OriginSkuStock getOriginSkuStock(Long skuId, SkuInfoDto skuInfoDto
    ) {
        Date date = new Date();
        OriginSkuStock originSkuStock = new OriginSkuStock();
        originSkuStock.setId(IDUtil.getSnowflakeId());
        originSkuStock.setSkuId(skuId);
        originSkuStock.setStoreId(skuInfoDto.getStoreId());
        originSkuStock.setOriginQuantity(skuInfoDto.getOriginQuantity());
        originSkuStock.setBalanceTime(date);
        originSkuStock.setParentId(0L);
        return originSkuStock;
    }


    /**
     * 初始化skuData By spu
     *
     * @param goodsSpu spu
     * @param goodssku sku
     * @return
     * @throws CustomsException
     */
    private void initSkuData(GoodsSpu goodsSpu, GoodsSku goodssku) throws CustomsException {
        goodssku.setId(IDUtil.getSnowflakeId());
        goodssku.setSpuId(goodsSpu.getSpuId());
        goodssku.setCatId(goodsSpu.getCatId());
        goodssku.setCode(billOrderNoService.genSkuCode(goodssku.getCode()));
        if (StringUtils.isNotBlank(goodsSpu.getCatName())) {
            goodssku.setCatName(goodsSpu.getCatName());
        }
        goodssku.setCreateTime(goodsSpu.getCreateTime());
        goodssku.setUpdateTime(goodsSpu.getUpdateTime());
        goodssku.setStatus("1");
    }

    /**
     * 初始化spu
     *
     * @param spu spu
     */
    private void initSpuData(GoodsSpu spu) {
        Date date = new Date();
        UserInfo userInfo = UserInfoThreadLocalUtils.get();
        Long userId = userInfo.getUserId();
        spu.setSpuId(IDUtil.getSnowflakeId());
        spu.setIsDelete("0");
        spu.setCreateTime(date);
        spu.setUpdateTime(date);
        spu.setCreateUser(userId);
        spu.setUpdateUser(userId);
    }

    /**
     * 初始化库存信息
     *
     * @param goodsStock 库存
     * @param skuId      skuId
     */
    private void initGoodsStock(GoodsStock goodsStock, Long skuId) {
        goodsStock.setSkuId(skuId);
        goodsStock.setQuantityIn(BigDecimal.ZERO);
        goodsStock.setQuantityOut(BigDecimal.ZERO);
        goodsStock.setWaitQuantityIn(BigDecimal.ZERO);
        goodsStock.setWaitQuantityOut(BigDecimal.ZERO);
    }
}
