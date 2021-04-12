package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.service.impl.origin.OriginSkuCostpriceServiceImpl;
import com.mrguo.service.impl.origin.OriginSkuStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品Service实现类
 *
 * @author mrguo
 */
@Service("goodsSkuDelServiceImpl")
public class GoodsSkuDelServiceImpl {

    @Autowired
    private GoodsskuMapper goodsskuMapper;
    @Autowired
    GoodsStockServiceImpl goodsStockService;
    @Autowired
    GoodsStockWarnServiceImpl goodsStockWarnService;
    @Autowired
    GoodsCostPriceServiceImpl goodsCostPriceService;
    @Autowired
    GoodsCostpriceOriginServiceImpl goodsCostpriceOriginService;
    @Autowired
    private GoodsPriceServiceImpl goodspriceService;
    @Autowired
    private GoodsUnitServiceImpl goodsUnitService;
    @Autowired
    private GoodsBarcodeServiceImpl goodsBarcodeService;
    @Autowired
    private OriginSkuCostpriceServiceImpl originSkuCostpriceService;
    @Autowired
    private OriginSkuStockServiceImpl originSkuStockService;

    /**
     * 删除所有skus
     */
    public void clearAllData() throws CustomsException {
    }

    /**
     * 删除sku相关的表数据
     */
    private void delSkuRelationData(Long id) throws CustomsException {

    }
}
