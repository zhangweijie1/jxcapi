package com.mrguo.service.impl.excle;

import com.alibaba.fastjson.JSONObject;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dto.goods.SkuInfoDto;
import com.mrguo.entity.bsd.Specs;
import com.mrguo.entity.bsd.Unit;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import com.mrguo.entity.goods.*;
import com.mrguo.entity.excle.ExcelImportSkuVo;
import com.mrguo.util.ExcelUtils;
import jxl.Sheet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName ExcleGoodsImportHandle
 * @Description 商品导入数据处理
 * @date 2020/7/268:29 AM
 * @updater 郭成兴
 * @updatedate 2020/7/268:29 AM
 */

@Component
public class ExcleGoodsImportHandle {

    private Integer[] specsIndex = new Integer[2];
    private Integer[] unitIndex = new Integer[2];
    private Integer[] barcodeIndex = new Integer[2];
    private Integer[] priceSaleIndex = new Integer[2];
    private Integer[] priceWholeSaleIndex = new Integer[2];
    private Integer[] priceMinSaleIndex = new Integer[2];
    private Integer[] pricePurchaseIndex = new Integer[2];
    private Integer[] stockWarnIndex = new Integer[2];
    private Integer weightIndex = null;
    private Integer remarksIndex = null;
    private Integer statusIndex = null;
    private List<String> catNameList = new ArrayList<>();
    private List<String> unitNameList = new ArrayList<>();
    private List<String> specsMultiNameList = new ArrayList<>();
    private List<Goodscat> catList = new ArrayList<>();
    private List<Unit> unitList = new ArrayList<>();
    private List<Specs> specsList = new ArrayList<>();
    private List<ExcelImportSkuVo> excelImportGoodsData;
    private Long storeId;

    public List<ExcelImportSkuVo> getExcleData() {
        return this.excelImportGoodsData;
    }

    public List<String> getCatNameList() {
        List<String> list = this.catNameList;
        return removeDuplicate(list);
    }

    public List<String> getUnitNameList() {
        List<String> list = this.unitNameList;
        return removeDuplicate(list);
    }

    public List<String> getSpecsMultiNameList() {
        List<String> list = this.specsMultiNameList;
        return removeDuplicate(list);
    }

    public void setCatList(List<Goodscat> goodscatList) {
        this.catList = goodscatList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public void setSpecsList(List<Specs> specsList) {
        this.specsList = specsList;
    }

    public void initData(Sheet sheet, Long storeId) {
        this.storeId = storeId;
        this.excelImportGoodsData = parseExcleData(sheet);
    }

    public void valiData() throws CustomsException {
        List<String> catNameList = this.getCatNameList();
        if (this.catList.size() < catNameList.size()) {
            List<String> postCatNames = this.catList.stream().map(Goodscat::getName).collect(Collectors.toList());
            catNameList.removeAll(postCatNames);
            throw new CustomsException("商品分类" + catNameList + "不存在，请选录入！");
        }

        List<String> specsNameList = this.getSpecsMultiNameList();
        if (this.specsList.size() < specsNameList.size()) {
            List<String> postSpecsName = this.specsList.stream().map(Specs::getName).collect(Collectors.toList());
            specsNameList.removeAll(postSpecsName);
            throw new CustomsException("规格" + specsNameList.toString() + "不存在，请选录入！");
        }

        List<String> unitNameList = this.getUnitNameList();
        if (this.unitList.size() < unitNameList.size()) {
            List<String> postUnitNames = this.unitList.stream().map(Unit::getName).collect(Collectors.toList());
            unitNameList.removeAll(postUnitNames);
            throw new CustomsException("单位" + unitNameList + "不存在，请选录入！");
        }

        for (ExcelImportSkuVo excelImportSkuVo : this.excelImportGoodsData) {
            List<String> units = excelImportSkuVo.getUnits();
            for (int i = 0; i < units.size(); i++) {
                String unitStr = units.get(i);
                if (i == 0) {
                    if (StringUtils.isBlank(unitStr)) {
                        throw new CustomsException("基础单位必填！");
                    }
                } else {
                    if (StringUtils.isNotBlank(unitStr)) {
                        if (!unitStr.contains("=")) {
                            throw new CustomsException("副单位缺少换算系数,如:（箱=4）");
                        }
                    }
                }
            }
        }
    }

    /**
     * excle数据转化成goodsData新增
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/7/26 3:14 PM
     * @updater 郭成兴
     * @updatedate 2020/7/26 3:14 PM
     */
    public List<GoodsAssemblyVo> excleData2GoodsDataList(List<ExcelImportSkuVo> excelImportSkuVoList) throws CustomsException {
        ArrayList<GoodsAssemblyVo> goodsAssemblyVos = new ArrayList<>();
        // 存放spu的nama的list（当存在name时，说明已存在spu）
        // { spuName: GoodsDto}
        HashMap<String, GoodsAssemblyVo> spusMap = new HashMap<>();
        for (ExcelImportSkuVo excelData : excelImportSkuVoList) {
            String name = excelData.getName();
            if (!spusMap.containsKey(name)) {
                GoodsAssemblyVo goodsAssemblyVo = new GoodsAssemblyVo();
                GoodsSpu goodsSpu = getGoodsspuByExcleData(excelData);
                ArrayList<GoodSkuAllInfoVo> goodSkuAllInfoVoList = new ArrayList<>();
                //
                GoodSkuAllInfoVo skuDtoByExcleData = getSkuDtoByExcleData(excelData);
                goodSkuAllInfoVoList.add(skuDtoByExcleData);
                goodsAssemblyVo.setGoodsSpu(goodsSpu);
                goodsAssemblyVo.setGoodSkuAllInfoVoList(goodSkuAllInfoVoList);
                //
                goodsAssemblyVos.add(goodsAssemblyVo);
                spusMap.put(name, goodsAssemblyVo);
            } else {
                GoodsAssemblyVo goodsAssemblyVo = spusMap.get(name);
                List<GoodSkuAllInfoVo> goodSkuAllInfoVoList = goodsAssemblyVo.getGoodSkuAllInfoVoList();
                GoodSkuAllInfoVo skuDtoByExcleData = getSkuDtoByExcleData(excelData);
                goodSkuAllInfoVoList.add(skuDtoByExcleData);
            }
        }
        return goodsAssemblyVos;
    }

    private List<String> removeDuplicate(List<String> list) {
        HashSet h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    private GoodsSpu getGoodsspuByExcleData(ExcelImportSkuVo data) {
        GoodsSpu goodsSpu = new GoodsSpu();
        goodsSpu.setIsDelete("0");
        Goodscat goodscat = getCatByName(data.getCat());
        goodsSpu.setCatId(goodscat.getId());
        goodsSpu.setName(data.getName());
        goodsSpu.setIsEnableSpecs("1");
        return goodsSpu;
    }

    public GoodSkuAllInfoVo getSkuDtoByExcleData(ExcelImportSkuVo data) throws CustomsException {
        GoodSkuAllInfoVo goodSkuAllInfoVo = new GoodSkuAllInfoVo();
        // Goodssku
        GoodsSku goodssku = new GoodsSku();
        Goodscat goodscat = getCatByName(data.getCat());
        goodssku.setCode(data.getCode());
        goodssku.setName(data.getName());
        goodssku.setSpecs(getSpecsJsonByExcleData(data));
        goodssku.setCatId(goodscat.getId());
        goodssku.setCatName(goodscat.getName());
        String isEnable = "启用".equals(data.getIsEnable()) ? "1" : "0";
        goodssku.setIsEnable(isEnable);
        goodssku.setRemarks(data.getRemarks());
        if (data.getSpecs().size() > 1) {
            goodssku.setIsEnableSpecs("1");
        } else {
            goodssku.setIsEnableSpecs("0");
        }
        goodSkuAllInfoVo.setGoodsSku(goodssku);
        // goodspriceList
        List<GoodsPrice> priceListData = getPriceListDataByExcleData(data);
        goodSkuAllInfoVo.setGoodsPriceList(priceListData);
        // goodsUnitList
        List<GoodsUnit> unitListData = getUnitListDataByExcleData(data);
        goodSkuAllInfoVo.setGoodsUnitList(unitListData);
        // goodsBarcodeList
        List<GoodsBarcode> barcodeListData = getBarcodeListDataByExcleData(data);
        goodSkuAllInfoVo.setGoodsBarcodeList(barcodeListData);
        // goodsStockList
        List<SkuInfoDto> stockListDataByExcleData = getStockListDataByExcleData(data);
        goodSkuAllInfoVo.setGoodsStockList(stockListDataByExcleData);
        // goodsStockWarnList
        List<GoodsStockWarn> stockWarnListDataByExcleData = getStockWarnListDataByExcleData(data);
        goodSkuAllInfoVo.setGoodsStockWarnList(stockWarnListDataByExcleData);
        return goodSkuAllInfoVo;
    }

    private String getSpecsJsonByExcleData(ExcelImportSkuVo data) {
        List<String> specs = data.getSpecs();
        if (specs.size() == 0) {
            return "";
        }
        if (specs.size() > 1) {
            JSONObject jsonObject = new JSONObject();
            for (String str : specs) {
                Specs s = getSpecsByName(str);
                jsonObject.put(String.valueOf(s.getPid()), s.getName());
            }
            return jsonObject.toString();
        } else {
            return specs.get(0);
        }
    }

    private List<GoodsPrice> getPriceListDataByExcleData(ExcelImportSkuVo data) {
        ArrayList<GoodsPrice> goodsPrices = new ArrayList<>();
        List<String> units = data.getUnits();
        List<BigDecimal> priceSales = data.getPriceSale();
        List<BigDecimal> priceMinSales = data.getPriceMinSale();
        List<BigDecimal> priceWholeSales = data.getPriceWholeSale();
        List<BigDecimal> pricePurchases = data.getPricePurchase();
        for (int i = 0; i < units.size(); i++) {
            GoodsPrice goodsprice = new GoodsPrice();
            BigDecimal priceSale = priceSales.get(i);
            BigDecimal priceMinSale = priceMinSales.get(i);
            BigDecimal priceWholeSale = priceWholeSales.get(i);
            BigDecimal pricePurchase = pricePurchases.get(i);
            GoodsUnit goodsUnit = getGoodUnitByName(units.get(i));
            goodsprice.setUnitId(goodsUnit.getUnitId());
            JSONObject price = new JSONObject();
            price.put("0", pricePurchase);
            price.put("1_0", priceWholeSale);
            price.put("1_1", priceSale);
            price.put("1_2", priceMinSale);
            goodsprice.setPrice(price.toJSONString());
            //
            goodsPrices.add(goodsprice);
        }
        return goodsPrices;
    }

    private List<GoodsUnit> getUnitListDataByExcleData(ExcelImportSkuVo data) throws CustomsException {
        ArrayList<GoodsUnit> goodsUnits = new ArrayList<>();
        List<String> units = data.getUnits();
        for (int i = 0; i < units.size(); i++) {
            String unitStr = units.get(i);
            if (i != 0) {
                if (StringUtils.isNotBlank(unitStr) && !unitStr.contains("=")) {
                    throw new CustomsException("副单位缺少换算系数，格式：'箱=12'");
                }
            }
            GoodsUnit goodsUnit = getGoodUnitByName(unitStr);
            goodsUnits.add(goodsUnit);
        }
        return goodsUnits;
    }

    private List<GoodsBarcode> getBarcodeListDataByExcleData(ExcelImportSkuVo data) {
        ArrayList<GoodsBarcode> barcodes = new ArrayList<>();
        List<String> units = data.getUnits();
        List<String> barcodesExcle = data.getBarcodes();
        for (int i = 0; i < units.size(); i++) {
            GoodsBarcode goodsBarcode = new GoodsBarcode();
            GoodsUnit goodsUnit = getGoodUnitByName(units.get(i));
            goodsBarcode.setUnitId(goodsUnit.getUnitId());
            String barcode = null;
            if (barcodesExcle.size() >= i + 1) {
                barcode = barcodesExcle.get(i);
            } else {
                barcode = "";
            }
            goodsBarcode.setBarcode(barcode);
            barcodes.add(goodsBarcode);
        }
        return barcodes;
    }

    private List<SkuInfoDto> getStockListDataByExcleData(ExcelImportSkuVo data) {
        ArrayList<SkuInfoDto> stocks = new ArrayList<>();
        SkuInfoDto skuInfoDto = new SkuInfoDto();
        skuInfoDto.setOriginQuantity(data.getOriginQty());
        skuInfoDto.setStoreId(this.storeId);
        skuInfoDto.setCostPrice(data.getOriginPrice());
        stocks.add(skuInfoDto);
        return stocks;
    }

    private List<GoodsStockWarn> getStockWarnListDataByExcleData(ExcelImportSkuVo data) {
        ArrayList<GoodsStockWarn> stocks = new ArrayList<>();
        GoodsStockWarn goodsStockWarn = new GoodsStockWarn();
        List<BigDecimal> stockWarn = data.getStockWarn();
        goodsStockWarn.setMinQtyWarning(stockWarn.get(0));
        goodsStockWarn.setMaxQtyWarning(stockWarn.get(1));
        goodsStockWarn.setStoreId(this.storeId);
        stocks.add(goodsStockWarn);
        return stocks;
    }

    private Goodscat getCatByName(String name) {
        List<Goodscat> goodscatList = this.catList.stream().filter(item -> {
            return name.equals(item.getName());
        }).collect(Collectors.toList());
        Goodscat goodscat = goodscatList.get(0);
        return goodscat;
    }

    private GoodsUnit getGoodUnitByName(String name) {
        String[] split = name.split("=");
        String unitName = split[0];
        List<Unit> list = this.unitList.stream().filter(item -> {
            return unitName.equals(item.getName());
        }).collect(Collectors.toList());
        Unit u = list.get(0);
        GoodsUnit goodsUnit = new GoodsUnit();
        goodsUnit.setUnitId(u.getId());
        if (split.length == 1) {
            goodsUnit.setIsBase("1");
            goodsUnit.setMulti(BigDecimal.ZERO);
            goodsUnit.setUnitName(split[0]);
        } else {
            goodsUnit.setIsBase("0");
            goodsUnit.setMulti(new BigDecimal(split[1]));
            goodsUnit.setUnitName(split[0]);
        }
        return goodsUnit;
    }

    private Specs getSpecsByName(String name) {
        List<Specs> list = this.specsList.stream().filter(item -> {
            return name.equals(item.getName());
        }).collect(Collectors.toList());
        Specs e = list.get(0);
        return e;
    }

    private List<ExcelImportSkuVo> parseExcleData(Sheet sheet) {
        getSomeIndex(sheet);
        //
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        int startParseRowIndex = 4;
        ArrayList<ExcelImportSkuVo> excelGoodsListData = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            if (i >= startParseRowIndex) {
                ExcelImportSkuVo excelImportSkuVo = new ExcelImportSkuVo();
                ArrayList<String> specsList = new ArrayList<>();
                ArrayList<String> unitList = new ArrayList<>();
                ArrayList<String> barcodeList = new ArrayList<>();
                ArrayList<BigDecimal> priceSaleList = new ArrayList<>();
                ArrayList<BigDecimal> priceMinSaleList = new ArrayList<>();
                ArrayList<BigDecimal> priceWholeSaleList = new ArrayList<>();
                ArrayList<BigDecimal> pricePurchaseList = new ArrayList<>();
                ArrayList<BigDecimal> stockWarnList = new ArrayList<>();
                for (int j = 0; j < columns; j++) {
                    String value = ExcelUtils.getContent(sheet, i, j);
                    System.out.println("第" + i + "行，第" + j + "列:" + String.valueOf(value));
                    switch (j) {
                        case 0:
                            this.catNameList.add(value);
                            excelImportSkuVo.setCat(value);
                            break;
                        case 1:
                            excelImportSkuVo.setCode(value);
                            break;
                        case 2:
                            excelImportSkuVo.setName(value);
                            break;
                        case 3:
                            excelImportSkuVo.setOriginQty(new BigDecimal(value));
                            break;
                        case 4:
                            excelImportSkuVo.setOriginPrice(new BigDecimal(value));
                            break;
                        case 5:
                            excelImportSkuVo.setOriginTotalPrice(new BigDecimal(value));
                            break;
                        default:
                            break;
                    }
                    if (j >= this.specsIndex[0] && j <= this.specsIndex[1]) {
                        if (value != null) {
                            specsList.add(value);
                        }
                    }
                    if (j >= this.unitIndex[0] && j <= this.unitIndex[1]) {
                        if (value != null) {
                            unitList.add(value);
                            String[] split = value.split("=");
                            String unitName = split[0];
                            this.unitNameList.add(unitName);
                        }
                    }
                    if (j >= this.barcodeIndex[0] && j <= this.barcodeIndex[1]) {
                        if (value != null) {
                            barcodeList.add(value);
                        }
                    }
                    if (j >= this.priceSaleIndex[0] && j <= this.priceSaleIndex[1]) {
                        if (value != null) {
                            priceSaleList.add(new BigDecimal(value));
                        }
                    }
                    if (j >= this.priceMinSaleIndex[0] && j <= this.priceMinSaleIndex[1]) {
                        if (value != null) {
                            priceMinSaleList.add(new BigDecimal(value));
                        }
                    }
                    if (j >= this.priceWholeSaleIndex[0] && j <= this.priceWholeSaleIndex[1]) {
                        if (value != null) {
                            priceWholeSaleList.add(new BigDecimal(value));
                        }
                    }
                    if (j >= this.pricePurchaseIndex[0] && j <= this.pricePurchaseIndex[1]) {
                        if (value != null) {
                            pricePurchaseList.add(new BigDecimal(value));
                        }
                    }
                    if (j >= this.stockWarnIndex[0] && j <= this.stockWarnIndex[1]) {
                        if (value != null) {
                            stockWarnList.add(new BigDecimal(value));
                        }
                    }
                    if (j == this.weightIndex) {
                        if (value != null) {
                            excelImportSkuVo.setWeight(new BigDecimal(value));
                        }
                    }
                    if (j == this.remarksIndex) {
                        if (value != null) {
                            excelImportSkuVo.setRemarks(value);
                        }
                    }
                    if (j == this.statusIndex) {
                        if (value != null) {
                            excelImportSkuVo.setIsEnable(value);
                        }
                    }
                }
                if (specsList.size() > 1) {
                    this.specsMultiNameList.addAll(specsList);
                }
                excelImportSkuVo.setSpecs(specsList);
                excelImportSkuVo.setUnits(unitList);
                excelImportSkuVo.setBarcodes(barcodeList);
                excelImportSkuVo.setPriceSale(priceSaleList);
                excelImportSkuVo.setPriceMinSale(priceMinSaleList);
                excelImportSkuVo.setPriceWholeSale(priceWholeSaleList);
                excelImportSkuVo.setPricePurchase(pricePurchaseList);
                excelImportSkuVo.setStockWarn(stockWarnList);
                //
                excelGoodsListData.add(excelImportSkuVo);
            }
        }
        return excelGoodsListData;
    }

    private void getSomeIndex(Sheet sheet) {
        int columns = sheet.getColumns();
        int rowIndex = 2;
        //
        Integer specsStart = null;
        Integer unitStart = null;
        Integer barcodeStart = null;
        Integer priceSaleStart = null;
        Integer priceWholeSaleStart = null;
        Integer priceMinSaleStart = null;
        Integer pricePurchaseStart = null;
        for (int j = 0; j < columns; j++) {
            String content = ExcelUtils.getContent(sheet, rowIndex, j);
            String value = content == null ? "" : content;
            switch (value) {
                case "多规格":
                    if (specsStart == null) {
                        specsStart = j;
                    }
                    break;
                case "多单位":
                    if (unitStart == null) {
                        unitStart = j;
                    }
                    break;
                case "条形码":
                    if (barcodeStart == null) {
                        barcodeStart = j;
                    }
                    break;
                case "零售价":
                    if (priceSaleStart == null) {
                        priceSaleStart = j;
                    }
                    break;
                case "批发价":
                    if (priceWholeSaleStart == null) {
                        priceWholeSaleStart = j;
                    }
                    break;
                case "最低售价":
                    if (priceMinSaleStart == null) {
                        priceMinSaleStart = j;
                    }
                    break;
                case "参考进货价":
                    if (pricePurchaseStart == null) {
                        pricePurchaseStart = j;
                    }
                    break;
                default:
                    break;
            }
        }
        int specsLen = unitStart - specsStart;
        int unitLen = barcodeStart - unitStart;
        this.specsIndex[0] = specsStart;
        this.specsIndex[1] = specsStart + specsLen - 1;
        this.unitIndex[0] = unitStart;
        this.unitIndex[1] = unitStart + unitLen - 1;
        this.barcodeIndex[0] = barcodeStart;
        this.barcodeIndex[1] = barcodeStart + unitLen - 1;
        this.priceSaleIndex[0] = priceSaleStart;
        this.priceSaleIndex[1] = priceSaleStart + unitLen - 1;
        this.priceWholeSaleIndex[0] = priceWholeSaleStart;
        this.priceWholeSaleIndex[1] = priceWholeSaleStart + unitLen - 1;
        this.priceMinSaleIndex[0] = priceMinSaleStart;
        this.priceMinSaleIndex[1] = priceMinSaleStart + unitLen - 1;
        this.pricePurchaseIndex[0] = pricePurchaseStart;
        this.pricePurchaseIndex[1] = pricePurchaseStart + unitLen - 1;
        this.stockWarnIndex[0] = this.pricePurchaseIndex[1] + 1;
        this.stockWarnIndex[1] = this.pricePurchaseIndex[1] + 2;
        this.weightIndex = this.stockWarnIndex[1] + 1;
        this.remarksIndex = this.weightIndex + 1;
        this.statusIndex = this.remarksIndex + 1;
    }
}
