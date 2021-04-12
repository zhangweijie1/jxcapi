package com.mrguo.service.impl.basedata.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bsd.StockMapper;
import com.mrguo.dto.OptionStore;
import com.mrguo.service.impl.basedata.StoreOptionsServiceImpl;
import com.mrguo.service.impl.basedata.StoreServiceImpl;
import com.mrguo.vo.basedata.StockOfSkuVo;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.enums.ElmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/1/410:41 PM
 * @updater 郭成兴
 * @updatedate 2020/1/410:41 PM
 */
@Service("goodsStockServiceImpl")
public class GoodsStockServiceImpl extends BaseServiceImpl<GoodsStock> {

    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StoreServiceImpl storeService;
    @Autowired
    private StoreOptionsServiceImpl storeOptionsService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<GoodsStock> getMapper() {
        return stockMapper;
    }

    /**
     * 新增多条数据
     *
     * @param stockList
     * @return
     */
    void addListData(List<GoodsStock> stockList) throws CustomsException {
        if (stockMapper.insertList(stockList) < 0) {
            throw new CustomsException("批量插入库存失败！");
        }
    }

    /**
     * 查询库存列表
     *
     * @param pageParams
     * @return
     */
    public Result getStockList(PageParams<StockOfSkuVo> pageParams) {
        Page<StockOfSkuVo> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        String storeId = (String) data.get("storeId");
        if (storeId == null) {
            return Result.fail("仓库不能为空!");
        }
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        try {
            valiListStock(data, userInfo);
        } catch (CustomsException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
        if (ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            // 是管理员
            if ("-1".equals(storeId)) {
                // 全部仓库
                List<OptionStore> storeAllOptions = storeOptionsService.getStoreAllOptions();
                List<String> storeIds = storeAllOptions.stream().map(item -> {
                    return String.valueOf(item.getValue());
                }).collect(Collectors.toList());
                data.put("storeIds", storeIds);
            } else {
                // 单独门店，单独仓库
                ArrayList<String> storeIds = new ArrayList<>();
                storeIds.add(storeId);
                data.put("storeIds", storeIds);
            }
            page.setRecords(stockMapper.selectStocksPage(page, data));
            return Result.ok(page);
        }
        // 非管理员
        SysDataPermission dataPermission = userInfo.getDataPermission();
        List<String> relationStores = dataPermission.getRelationStores();
        data.put("storeIds", relationStores);
        page.setRecords(stockMapper.selectStocksPage(page, data));
        return Result.ok(page);
    }

    /**
     * 查询仓库数据by skuids
     *
     * @param skuIds
     * @return
     */
    public List<GoodsStock> listStockMergeStoreBySkuIds(List<Long> skuIds) {
        List<String> skustrIds = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        return stockMapper.selectStockMergeStoreBySkuIds(skustrIds);
    }

    public GoodsStock getStockBySkuId(Long skuId) throws CustomsException {
        return stockMapper.getStockMergeStoreBySkuId(skuId);
    }

    public List<GoodsStock> getStockGroupStoreBySkuId(Long skuId) {
        return stockMapper.selectStockGroupStoreBySkuId(skuId);
    }

    public List<GoodsStock> listStockBySkuIdsAndStoreId(List<Long> skuIds, Long storeId) {
        List<String> list = skuIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<GoodsStock> goodsStocks = stockMapper.selectStockBySkuIdsAndStoreId(list, storeId);
        if (goodsStocks.size() == 0) {
            // sku没有该仓库的信息，我们可以新增
            ArrayList<GoodsStock> goodsStocksParams = new ArrayList<>();
            for (Long skuId : skuIds) {
                GoodsStock goodsStock = new GoodsStock();
                goodsStock.setSkuId(skuId);
                goodsStock.setStoreId(storeId);
                goodsStock.setWaitQuantityIn(BigDecimal.ZERO);
                goodsStock.setWaitQuantityOut(BigDecimal.ZERO);
                goodsStock.setQuantityIn(BigDecimal.ZERO);
                goodsStock.setQuantityOut(BigDecimal.ZERO);
                goodsStocksParams.add(goodsStock);
            }
            stockMapper.insertList(goodsStocksParams);
            return goodsStocksParams;
        } else {
            return goodsStocks;
        }
    }

    /**
     * 校验查询stock
     */
    private void valiListStock(Map<String, Object> data, UserInfo userInfo) throws CustomsException {
        String storeId = (String) data.get("storeId");
        SysDataPermission dataPermission = userInfo.getDataPermission();
        //
        if (!ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            List<String> relationStores = dataPermission.getRelationStores();
            // 非管理员
            if (!"-1".equals(storeId) && !relationStores.contains(storeId)) {
                throw new CustomsException("您没有查看该仓库库存的权限!");
            }
        }
    }
}
