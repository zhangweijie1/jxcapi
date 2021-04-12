package com.mrguo.service.impl.excle;

import com.alibaba.fastjson.JSONObject;
import com.mrguo.entity.excle.ExcelExportSkuVo;
import com.mrguo.entity.goods.GoodsBarcode;
import com.mrguo.entity.goods.GoodsPrice;
import com.mrguo.entity.goods.GoodsUnit;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName ExcleGoodsExportHandle
 * @Description 商品导出数据处理
 * @date 2020/7/301:18 PM
 * @updater 郭成兴
 * @updatedate 2020/7/301:18 PM
 */
@Component
public class ExcleGoodsExportHandle {

    private Integer specsQty;

    private Integer unitQty;

    public ByteArrayOutputStream exportData(List<ExcelExportSkuVo> excelExportSkuVoList) throws IOException, WriteException {
        Map<String, Integer> maxSize = getMaxSize(excelExportSkuVoList);
        int specsQty = maxSize.get("specs");
        int unitQty = maxSize.get("unit");
        this.specsQty = specsQty;
        this.unitQty = unitQty;
        ExcleGoodsTempGenerater excleGoodsTempGenerater = new ExcleGoodsTempGenerater(specsQty, unitQty);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableWorkbook wwb = excleGoodsTempGenerater.createWritableSheet(os);
        WritableSheet sheet = wwb.getSheet(0);
        for (int i = 0; i < excelExportSkuVoList.size(); i++) {
            ExcelExportSkuVo data = excelExportSkuVoList.get(i);
            handleExcelExportGoodsData(i, sheet, data);
        }
        wwb.write();
        wwb.close();
        return os;
    }

    private Map<String, Integer> getMaxSize(List<ExcelExportSkuVo> excelExportSkuVoList) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("specs", 3);
        map.put("unit", 4);
        return map;
    }

    private void handleExcelExportGoodsData(int dataIndex,
                                            WritableSheet sheet,
                                            ExcelExportSkuVo data) throws WriteException {
        int startRow = 4;
        int rowIndex = startRow + dataIndex;
        Label catNameLabel = new Label(0, rowIndex, data.getCatName());
        Label codeLabel = new Label(1, rowIndex, data.getCode());
        Label nameLabel = new Label(2, rowIndex, data.getName());
        Label originQtyLabel = new Label(3, rowIndex, String.valueOf(data.getOriginQuantity()));
        Label originPriceLabel = new Label(4, rowIndex, String.valueOf(data.getOriginPrice()));
        //
        BigDecimal originPrice = data.getOriginPrice() == null
                ? BigDecimal.ZERO : data.getOriginPrice();
        BigDecimal originQty = data.getOriginQuantity() == null
                ? BigDecimal.ZERO : data.getOriginQuantity();
        DecimalFormat lf = new DecimalFormat("#.0000");
        BigDecimal originPriceTotal = originQty.multiply(originPrice);
        String originPriceTotalFormat = lf.format(originPriceTotal);
        //
        Label originTotalLabel = new Label(5, rowIndex, originPriceTotalFormat);
        //
        sheet.addCell(catNameLabel);
        sheet.addCell(codeLabel);
        sheet.addCell(nameLabel);
        sheet.addCell(originQtyLabel);
        sheet.addCell(originPriceLabel);
        sheet.addCell(originTotalLabel);
        // specs
        handleSpecsExcelData(rowIndex, sheet, data);
        // unit
        handleUnitExcleData(rowIndex, sheet, data);
        // barcode
        handleBarcodeExcleData(rowIndex, sheet, data);
        // price
        handlePriceExcleData(rowIndex, sheet, data);
        // stockwarn
        handleStockWarn(rowIndex, sheet, data);
        // weight
        handleWeight(rowIndex, sheet, data);
        // remarks
        handleRemarks(rowIndex, sheet, data);
        // status
        handleStatus(rowIndex, sheet, data);
    }

    private void handleSpecsExcelData(int rowIndex,
                                      WritableSheet sheet,
                                      ExcelExportSkuVo data) throws WriteException {
        int startCol = 6;
        String specs = data.getSpecs();
        String isEnableSpecs = data.getIsEnableSpecs();
        if ("0".equals(isEnableSpecs)) {
            // 单规格
            Label specsLabel = new Label(startCol, rowIndex, specs);
            sheet.addCell(specsLabel);
            return;
        }
        JSONObject specsObj = JSONObject.parseObject(specs);
        for (String str : specsObj.keySet()) {
            String specsName = (String) specsObj.get(str);
            Label specsLabel = new Label(startCol, rowIndex, specsName);
            sheet.addCell(specsLabel);
            startCol++;
        }
    }

    private void handleUnitExcleData(int rowIndex,
                                     WritableSheet sheet,
                                     ExcelExportSkuVo data) throws WriteException {

        int startCol = 6 + specsQty;
        List<GoodsUnit> goodsUnitList = data.getGoodsUnitList();
        for (GoodsUnit unit : goodsUnitList) {
            String unitName = "";
            if ("1".equals(unit.getIsBase())) {
                unitName = unit.getUnitName();
            } else {
                unitName = unit.getUnitName() + "=" + unit.getMulti();
            }
            Label specsLabel = new Label(startCol, rowIndex, unitName);
            sheet.addCell(specsLabel);
            startCol++;
        }
    }

    private void handleBarcodeExcleData(int rowIndex,
                                        WritableSheet sheet,
                                        ExcelExportSkuVo data) throws WriteException {

        int startCol = 6 + specsQty + unitQty;
        List<GoodsBarcode> barcodeList = data.getBarcodeList();
        List<GoodsUnit> goodsUnitList = data.getGoodsUnitList();
        for (GoodsUnit goodsUnit : goodsUnitList) {
            List<GoodsBarcode> collect = barcodeList.stream().filter(item -> {
                return goodsUnit.getUnitId().equals(item.getUnitId());
            }).collect(Collectors.toList());
            GoodsBarcode barcode = collect.get(0);
            String str = barcode.getBarcode();
            Label specsLabel = new Label(startCol, rowIndex, str);
            sheet.addCell(specsLabel);
            startCol++;
        }
    }

    private void handlePriceExcleData(int rowIndex,
                                      WritableSheet sheet,
                                      ExcelExportSkuVo data) throws WriteException {

        int startCol = 6 + specsQty + unitQty * 2;
        List<GoodsUnit> goodsUnitList = data.getGoodsUnitList();
        List<GoodsPrice> goodsPriceList = data.getGoodsPriceList();
        int i = 0;
        for (GoodsUnit goodsUnit : goodsUnitList) {
            List<GoodsPrice> collect = goodsPriceList.stream().filter(item -> {
                return goodsUnit.getUnitId().equals(item.getUnitId());
            }).collect(Collectors.toList());
            GoodsPrice goodsprice = collect.get(0);
            //
            String priceStr = goodsprice.getPrice();
            JSONObject priceObj = JSONObject.parseObject(priceStr);
            String priceSale = String.valueOf(priceObj.get("1_0"));
            String pricePifaSale = String.valueOf(priceObj.get("1_1"));
            String priceMinSale = String.valueOf(priceObj.get("1_2"));
            String pricePurchase = String.valueOf(priceObj.get("0"));
            //
            int priceSaleIndex = startCol + i;
            int pricePifaSaleIndex = startCol + unitQty + i;
            int priceMinSaleIndex = startCol + unitQty * 2 + i;
            int pricePurchaseIndex = startCol + unitQty * 3 + i;
            //
            Label priceSaleLabel = new Label(priceSaleIndex, rowIndex, priceSale);
            Label pricePifaSaleLabel = new Label(pricePifaSaleIndex, rowIndex, pricePifaSale);
            Label priceMinSaleLabel = new Label(priceMinSaleIndex, rowIndex, priceMinSale);
            Label pricePurchaseLabel = new Label(pricePurchaseIndex, rowIndex, pricePurchase);
            sheet.addCell(priceSaleLabel);
            sheet.addCell(pricePifaSaleLabel);
            sheet.addCell(priceMinSaleLabel);
            sheet.addCell(pricePurchaseLabel);
            i++;
        }
    }

    private void handleStockWarn(int rowIndex,
                                 WritableSheet sheet,
                                 ExcelExportSkuVo data) throws WriteException {
        int startCol = 6 + specsQty + unitQty * 6;
        String minQtyWarning = data.getMinQtyWarning() == null
                ? "" : String.valueOf(data.getMinQtyWarning());
        String maxQtyWarning = data.getMinQtyWarning() == null
                ? "" : String.valueOf(data.getMaxQtyWarning());
        //
        Label minQtyWarningLabel = new Label(startCol, rowIndex, minQtyWarning);
        Label maxQtyWarningLabel = new Label(startCol + 1, rowIndex, maxQtyWarning);
        sheet.addCell(minQtyWarningLabel);
        sheet.addCell(maxQtyWarningLabel);
    }

    private void handleWeight(int rowIndex,
                              WritableSheet sheet,
                              ExcelExportSkuVo data) throws WriteException {
        int startCol = 6 + specsQty + unitQty * 6 + 2;
        String value = data.getWeight() == null ? "" : String.valueOf(data.getWeight());
        Label label = new Label(startCol, rowIndex, value);
        sheet.addCell(label);
    }

    private void handleRemarks(int rowIndex,
                               WritableSheet sheet,
                               ExcelExportSkuVo data) throws WriteException {
        int startCol = 6 + specsQty + unitQty * 6 + 3;
        String value = data.getRemarks() == null ? "" : data.getRemarks();
        Label label = new Label(startCol, rowIndex, value);
        sheet.addCell(label);
    }

    private void handleStatus(int rowIndex,
                              WritableSheet sheet,
                              ExcelExportSkuVo data) throws WriteException {
        int startCol = 6 + specsQty + unitQty * 6 + 4;
        String isEnable = "1".equals(data.getIsEnable())
                ? "启用" : "停用";
        Label label = new Label(startCol, rowIndex, String.valueOf(isEnable));
        sheet.addCell(label);
    }
}
