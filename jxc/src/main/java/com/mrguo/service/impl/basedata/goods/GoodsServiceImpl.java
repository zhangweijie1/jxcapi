package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dto.goods.GoodAssemblyDto;
import com.mrguo.service.impl.basedata.goods.utils.GoodsExcleUtils;
import com.mrguo.service.impl.basedata.goods.utils.GoodsAddDataUtils;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import com.mrguo.entity.excle.ExcelExportSkuVo;
import com.mrguo.entity.goods.*;
import com.mrguo.entity.origin.OriginSkuCostprice;
import com.mrguo.entity.origin.OriginSkuStock;
import com.mrguo.service.impl.origin.OriginSkuCostpriceServiceImpl;
import com.mrguo.service.impl.origin.OriginSkuStockServiceImpl;
import com.mrguo.service.impl.excle.ExcleGoodsExportHandle;
import com.mrguo.service.impl.excle.ExcleGoodsTempGenerater;
import com.mrguo.service.impl.utils.ExcleServieUtils;
import com.mrguo.dto.goods.GoodAssemblyListDto;
import jxl.Sheet;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrguo.service.inter.bsd.GoodsService;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 商品Service实现类
 *
 * @author mrguo
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsSpuServiceImpl goodsspuService;
    @Autowired
    private GoodsSkuServiceImpl goodsskuService;
    @Autowired
    private GoodsPriceServiceImpl goodspriceService;
    @Autowired
    private GoodsLevelServiceImpl goodsLevelService;
    @Autowired
    private ExcleGoodsExportHandle excleGoodsExportHandle;
    @Autowired
    private GoodsUnitServiceImpl goodsUnitService;
    @Autowired
    private GoodsBarcodeServiceImpl goodsBarcodeService;
    @Autowired
    private GoodsCostPriceServiceImpl goodsCostPriceService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private GoodsStockWarnServiceImpl goodsStockWarnService;
    @Autowired
    private OriginSkuStockServiceImpl originSkuStockService;
    @Autowired
    private OriginSkuCostpriceServiceImpl originSkuCostpriceService;
    @Autowired
    private GoodsCostpriceOriginServiceImpl originCurrentCostpriceService;
    @Autowired
    private GoodsSkuDelServiceImpl goodsskuDelService;
    @Autowired
    private GoodsServiceHandleImport goodsServiceHandleImport;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private ExcleServieUtils excleServieUtils;

    /**
     * 组合添加方法（spu+sku一起新增）
     *
     * @param goodsAssemblyVo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addGoodAssemblyData(GoodsAssemblyVo goodsAssemblyVo) throws Exception {
        // 预处理数据
        GoodsAddDataUtils goodsAddDataUtils = new GoodsAddDataUtils(billOrderNoService);
        GoodAssemblyDto goodAssemblyDto = goodsAddDataUtils.splitAssemblyGood(goodsAssemblyVo);
        ArrayList<GoodAssemblyDto> assemblyDtoList = new ArrayList<>();
        // 新增
        assemblyDtoList.add(goodAssemblyDto);
        addAssemblyDtoListData(assemblyDtoList);
        return Result.ok();
    }

    @Override
    public Result importData(Sheet sheet) throws Exception {
        List<GoodAssemblyDto> goodAssemblyDtos = null;
        List<GoodSkuAllInfoVo> goodSkuAllInfoVos = null;
        try {
            GoodAssemblyListDto importData = goodsServiceHandleImport.getImportData(sheet);
            goodAssemblyDtos = importData.getGoodAssemblyDtos();
            goodSkuAllInfoVos = importData.getGoodSkuAllInfoVos();
        } catch (CustomsException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("数据格式有误！");
        }
        // 新增
        if (goodAssemblyDtos.size() > 0) {
            addImportData(goodAssemblyDtos);
        }
        // 修改
        for (GoodSkuAllInfoVo goodSkuAllInfoVo : goodSkuAllInfoVos) {
            goodsskuService.updateData(goodSkuAllInfoVo);
        }
        return Result.ok("导入成功！");
    }

    @Override
    public Result exportData(Long storeId) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", "1286193815601938433");
        List<ExcelExportSkuVo> exportGoodsData = goodsskuService.exportExcleData(map);
        // 单位（带系数），条形码，价格
        List<GoodsUnit> goodsUnitList = goodsUnitService.listAllData();
        List<GoodsBarcode> barcodeList = goodsBarcodeService.listAllData();
        List<GoodsPrice> goodsPriceList = goodspriceService.selectAll();
        GoodsExcleUtils.completeExcelExportGoodsData(exportGoodsData,
                goodsUnitList,
                barcodeList,
                goodsPriceList);
        ByteArrayOutputStream outputStream = excleGoodsExportHandle.exportData(exportGoodsData);
        // 转化成excle
        String fileName = "商品数据.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    @Override
    public Result createExcleTemp(Integer specsQty, Integer unitQty) throws IOException, WriteException {
        ExcleGoodsTempGenerater goodsTempCreate = new ExcleGoodsTempGenerater(specsQty, unitQty);
        ByteArrayOutputStream outputStream = goodsTempCreate.createExcle();
        String fileName = "商品导入模板.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    @Override
    public void clearAllData() throws CustomsException {
        goodsspuService.clearAllData();
        goodsskuDelService.clearAllData();
    }

    private Result addImportData(List<GoodAssemblyDto> paramLists) throws CustomsException {
        addAssemblyDtoListData(paramLists);
        return Result.ok();
    }

    /**
     * 新增多个商品组合
     *
     * @param assemblyDtos 多个商品组合
     * @throws CustomsException
     */
    private void addAssemblyDtoListData(List<GoodAssemblyDto> assemblyDtos) throws CustomsException {
        List<GoodsSpu> goodsSpuList = new ArrayList<>();
        List<GoodsSku> goodsSkuList = new ArrayList<>();
        List<GoodsPrice> goodsPriceList = new ArrayList<>();
        List<GoodsLevel> goodsLevelList = new ArrayList<>();
        List<GoodsUnit> goodsUnitList = new ArrayList<>();
        List<GoodsCostPrice> goodsCostPriceList = new ArrayList<>();
        List<GoodsBarcode> goodsBarcodeList = new ArrayList<>();
        List<GoodsStock> goodsStockList = new ArrayList<>();
        List<GoodsStockWarn> goodsStockWarnList = new ArrayList<>();
        List<OriginSkuStock> originSkuStockList = new ArrayList<>();
        List<OriginSkuCostprice> originSkuCostpriceList = new ArrayList<>();
        List<GoodsCostPriceOrigin> originCurrentCostpriceList = new ArrayList<>();
        for (GoodAssemblyDto goodAssemblyDto : assemblyDtos) {
            goodsSpuList.add(goodAssemblyDto.getGoodsSpu());
            goodsSkuList.addAll(goodAssemblyDto.getGoodsSkuList());
            goodsPriceList.addAll(goodAssemblyDto.getGoodsPriceList());
            goodsLevelList.addAll(goodAssemblyDto.getGoodsLevelList());
            goodsUnitList.addAll(goodAssemblyDto.getGoodsUnitList());
            goodsCostPriceList.addAll(goodAssemblyDto.getGoodsCostPriceList());
            goodsBarcodeList.addAll(goodAssemblyDto.getGoodsBarcodeList());
            goodsStockList.addAll(goodAssemblyDto.getGoodsStockList());
            goodsStockWarnList.addAll(goodAssemblyDto.getGoodsStockWarnList());
            originSkuStockList.addAll(goodAssemblyDto.getOriginSkuStockList());
            originSkuCostpriceList.addAll(goodAssemblyDto.getOriginSkuCostpriceList());
            originCurrentCostpriceList.addAll(goodAssemblyDto.getGoodsCostPriceOriginList());
        }
        // 1 新增spu
        goodsspuService.insertListData(goodsSpuList);
        // 2 新增sku
        goodsskuService.addListData(goodsSkuList);
        // unit
        goodsUnitService.addListData(goodsUnitList);
        // 3 价格数据
        goodspriceService.addPriceListData(goodsPriceList);
        //等级价
        goodsLevelService.addListData(goodsLevelList);
        // 3 成本价格
        goodsCostPriceService.addCostPriceListData(goodsCostPriceList);
        // 4 商品条形码
        goodsBarcodeService.addBarcodeListData(goodsBarcodeList);
        // 5 库存
        goodsStockService.addListData(goodsStockList);
        // 6 库存预警
        goodsStockWarnService.addStockWarnListData(goodsStockWarnList);
        // === 期初值涉及结存 ===
        // 1. 当期期初（商品成本，XX）
        // a. 商品成本
        originCurrentCostpriceService.insertListData(originCurrentCostpriceList);
        // 2. 结存期初（商品库存，商品成本）
        // a. 商品库存
        originSkuStockService.insertListData(originSkuStockList);
        // b. 商品成本
        originSkuCostpriceService.insertListData(originSkuCostpriceList);
    }
}
