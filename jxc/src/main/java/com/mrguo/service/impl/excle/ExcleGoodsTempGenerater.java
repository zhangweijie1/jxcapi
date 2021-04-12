package com.mrguo.service.impl.excle;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 郭成兴
 * @ClassName ExcleGoodsTempGenerater
 * @Description 商品模板生成
 * @date 2020/7/251:34 PM
 * @updater 郭成兴
 * @updatedate 2020/7/251:34 PM
 */

public class ExcleGoodsTempGenerater {

    private static final int START_ROW = 1;
    private static final int START_COL = 0;
    private static final int CELL_WIDTH = 14;
    private static final String BASES_TITLE = "基本信息";
    private static final String[] BASES_INFO = {"分类", "商品编号", "商品名称*"};
    private static final String ORIGIN_TITLE = "期初信息";
    private static final String[] ORIGIN_INFO = {"期初数量", "期初单价", "期初总金额"};
    private static final String SPECS_DETAIL_TITLE = "详细规格";
    private static final String SPECS_MULTI_TITLE = "多规格";
    private static final String SPECS_TITLE = "规格";
    private static final String UNIT_TITLE = "单位";
    private static final String UNIT_MULTI_TITLE = "多单位";
    private static final String UNIT_BASE_TITLE = "基础单位";
    private static final String BARCODE_TITLE = "条形码";
    private static final String PRICE_TITLE = "价格";
    private static final String[] PRICE_INFO = {"零售价", "批发价", "最低售价", "参考进货价"};
    private static final String STOCK_WARN_TITLE = "库存预警";
    private static final String[] STOCK_WARN_INFO = {"最低库存", "最高库存"};
    private static final String WEIGHT_TITLE = "单位重量";
    private static final String REMARKS_TITLE = "备注";
    private static final String STATUS_TITLE = "状态";
    private static final String TITLE = "1. “分类”，“规格”，“单位”，三个属性请预先在系统内录入。\n" +
            "2. 若是不同规格，同一个商品，请确保商品标题一致。（服装等行业）\n" +
            "2. 如表格中“商品编号”在系统中已存在，则为修改该编号商品。\n" +
            "3. “基本单位”为最小单位，副单位的换算系数必须 > 1。\n" +
            "4. “条形码”不能重复。";

    private Integer specsQty;
    private Integer unitQty;
    private ExcleCellFormatGenerater excleCellFormatGenerater;

    public ExcleGoodsTempGenerater(Integer specsQty, Integer unitQty) {
        this.specsQty = specsQty;
        this.unitQty = unitQty;
        this.excleCellFormatGenerater = new ExcleCellFormatGenerater();
    }

    /**
     * 生成一个空excle模板
     *
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public ByteArrayOutputStream createExcle() throws IOException, WriteException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableWorkbook wwb = createWritableSheet(os);
        wwb.write();
        wwb.close();
        return os;
    }

    /**
     * 生成一个可编辑excle
     *
     * @param os
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public WritableWorkbook createWritableSheet(ByteArrayOutputStream os) throws IOException, WriteException {
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet wwbSheet = wwb.createSheet("商品资料", 0);
        handleTitle(wwbSheet, TITLE);
        handleBaseInfo(wwbSheet);
        handleOrigin(wwbSheet);
        handleSpecsDetail(wwbSheet);
        handlePrice(wwbSheet);
        handleStockWarn(wwbSheet);
        handleUnitWeight(wwbSheet);
        handleRemarks(wwbSheet);
        handleStatus(wwbSheet);
        return wwb;
    }

    private void handleBaseInfo(WritableSheet sheet) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL;
        // 需要的长度
        int cl = BASES_INFO.length;
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        WritableCellFormat title3CellFormat = getTitle3CellFormat();
        for (int c = 0; c < cl; c++) {
            sheet.setColumnView(c + startC, CELL_WIDTH);
            for (int r = 0; r < rl; r++) {
                if (c == startC && r == startR) {
                    Label masterLabel = new Label(startC, startR, BASES_TITLE);
                    masterLabel.setCellFormat(title1CellFormat);
                    sheet.addCell(masterLabel);
                }
                if (r == rl - 1) {
                    Label label = new Label(c + startC, r + startR, BASES_INFO[c]);
                    label.setCellFormat(title3CellFormat);
                    sheet.addCell(label);
                }
            }
        }
        // 第x+1列，y+1行,  到, m+1列，n+1行合并 (四个点定义了两个坐标，左上角和右下角)
        sheet.mergeCells(startC, startR, startC + cl - 1, startR + 1);
    }

    private void handleOrigin(WritableSheet sheet) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL + BASES_INFO.length;
        // 需要的长度
        int cl = ORIGIN_INFO.length;
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        WritableCellFormat title3CellFormat = getTitle3CellFormat();
        for (int c = 0; c < cl; c++) {
            sheet.setColumnView(c + startC, CELL_WIDTH);
            for (int r = 0; r < rl; r++) {
                if (c == 0 && r == startR) {
                    Label masterLabel = new Label(startC, startR, ORIGIN_TITLE);
                    masterLabel.setCellFormat(title1CellFormat);
                    sheet.addCell(masterLabel);
                }
                if (r == rl - 1) {
                    Label label = new Label(c + startC, r + startR, ORIGIN_INFO[c]);
                    label.setCellFormat(title3CellFormat);
                    sheet.addCell(label);
                }
            }
        }
        // 第x+1列，y+1行,  到, m+1列，n+1行合并 (四个点定义了两个坐标，左上角和右下角)
        sheet.mergeCells(startC, startR, startC + cl - 1, startR + 1);
    }

    private void handleSpecsDetail(WritableSheet sheet) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL + BASES_INFO.length + ORIGIN_INFO.length;
        // 需要的长度
        int cl = getSpecsDetailLen();
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        WritableCellFormat title2CellFormat = getTitle2CellFormat();
        WritableCellFormat title3CellFormat = getTitle3CellFormat();
        for (int c = 0; c < cl; c++) {
            int currentC = startC + c;
            for (int r = 0; r < rl; r++) {
                int currentR = startR + r;
                if (c == 0 && r == startR) {
                    Label masterLabel = new Label(startC, startR, SPECS_DETAIL_TITLE);
                    masterLabel.setCellFormat(title1CellFormat);
                    sheet.addCell(masterLabel);
                }
                if (r == rl - 2) {
                    if (c < getSpecsLen()) {
                        Label label = new Label(currentC, currentR, SPECS_MULTI_TITLE);
                        label.setCellFormat(title2CellFormat);
                        sheet.addCell(label);
                    }
                    if (c >= getSpecsLen() && c < getSpecsLen() + getUnitLen()) {
                        Label label = new Label(currentC, currentR, UNIT_MULTI_TITLE);
                        label.setCellFormat(title2CellFormat);
                        sheet.addCell(label);
                    }
                    if (c >= getSpecsLen() + getUnitLen()) {
                        Label label = new Label(currentC, currentR, BARCODE_TITLE);
                        label.setCellFormat(title2CellFormat);
                        sheet.addCell(label);
                    }
                }
                if (r == rl - 1) {
                    if (c < getSpecsLen()) {
                        Label label = new Label(currentC, currentR, SPECS_TITLE + (c + 1));
                        label.setCellFormat(title3CellFormat);
                        sheet.addCell(label);
                    }
                    if (c >= getSpecsLen() && c < getSpecsLen() + getUnitLen()) {
                        int unitIndex = c - getSpecsLen() + 1;
                        Label label = null;
                        if (unitIndex == 1) {
                            label = new Label(currentC, currentR, UNIT_BASE_TITLE + "*");
                        } else {
                            label = new Label(currentC, currentR, "副单位" + (unitIndex - 1));
                        }
                        label.setCellFormat(title3CellFormat);
                        sheet.addCell(label);
                    }
                    if (c >= getSpecsLen() + getUnitLen()) {
                        int barcodeIndex = c - getSpecsLen() - getUnitLen() + 1;
                        Label label = null;
                        if (barcodeIndex == 1) {
                            label = new Label(currentC, currentR, UNIT_BASE_TITLE);
                        } else {
                            label = new Label(currentC, currentR, "副单位" + (barcodeIndex - 1));
                        }
                        label.setCellFormat(title3CellFormat);
                        sheet.addCell(label);
                    }
                }
            }
        }
        // 第x+1列，y+1行,  到, m+1列，n+1行合并 (四个点定义了两个坐标，左上角和右下角)
        sheet.mergeCells(startC, startR, startC + cl - 1, startR);
        // 规格，单位，条形码
        sheet.mergeCells(startC, startR + 1, startC + getSpecsLen() - 1, startR + 1);
        sheet.mergeCells(startC + getSpecsLen(), startR + 1, startC + getSpecsLen() + getUnitLen() - 1, startR + 1);
        sheet.mergeCells(startC + getSpecsLen() + getUnitLen(), startR + 1, startC + getSpecsLen() + getUnitLen() + getUnitLen() - 1, startR + 1);
    }

    private void handlePrice(WritableSheet sheet) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL + BASES_INFO.length + ORIGIN_INFO.length + getSpecsDetailLen();
        // 需要的长度
        int cl = getPriceLen();
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        WritableCellFormat title2CellFormat = getTitle2CellFormat();
        WritableCellFormat title3CellFormat = getTitle3CellFormat();
        for (int c = 0; c < cl; c++) {
            int currentC = startC + c;
            int unitLen = getUnitLen();
            int index = c % unitLen;
            int priceIndex = (int) Math.round(c / unitLen);
            for (int r = 0; r < rl; r++) {
                int currentR = startR + r;
                if (c == 0 && r == startR) {
                    Label masterLabel = new Label(startC, startR, PRICE_TITLE);
                    masterLabel.setCellFormat(title1CellFormat);
                    sheet.addCell(masterLabel);
                    sheet.mergeCells(startC, startR, startC + getPriceLen() - 1, startR);
                }
                // 第二行
                if (r == rl - 2) {
                    if (index == 0) {
                        Label label = new Label(currentC, currentR, PRICE_INFO[priceIndex]);
                        label.setCellFormat(title2CellFormat);
                        sheet.addCell(label);
                        sheet.mergeCells(startC + priceIndex * unitLen, startR + 1, startC + (priceIndex + 1) * unitLen - 1, startR + 1);
                    }
                }
                if (r == rl - 1) {
                    Label label = null;
                    if (index == 0) {
                        label = new Label(currentC, currentR, UNIT_BASE_TITLE);
                    } else {
                        label = new Label(currentC, currentR, "副单位" + (index));
                    }
                    label.setCellFormat(title3CellFormat);
                    sheet.addCell(label);
                }
            }
        }
    }

    private void handleStockWarn(WritableSheet sheet) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL + BASES_INFO.length + ORIGIN_INFO.length + getSpecsDetailLen() + getPriceLen();
        // 需要的长度
        int cl = STOCK_WARN_INFO.length;
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        WritableCellFormat title3CellFormat = getTitle3CellFormat();
        for (int c = 0; c < cl; c++) {
            int currentC = startC + c;
            for (int r = 0; r < rl; r++) {
                int currentR = startR + r;
                if (r == 0 && c == 0) {
                    Label masterLabel = new Label(currentC, currentR, STOCK_WARN_TITLE);
                    masterLabel.setCellFormat(title1CellFormat);
                    sheet.addCell(masterLabel);
                }
                if (r == 2) {
                    Label label = new Label(currentC, currentR, STOCK_WARN_INFO[c]);
                    label.setCellFormat(title3CellFormat);
                    sheet.addCell(label);
                }
            }
        }
        sheet.mergeCells(startC, startR, startC + STOCK_WARN_INFO.length - 1, startR + 1);
    }

    private void handleUnitWeight(WritableSheet sheet) throws WriteException {
        this.hendleSingleCol(sheet, WEIGHT_TITLE, 0);
    }

    private void handleRemarks(WritableSheet sheet) throws WriteException {
        this.hendleSingleCol(sheet, REMARKS_TITLE, 1);
    }

    private void handleStatus(WritableSheet sheet) throws WriteException {
        this.hendleSingleCol(sheet, STATUS_TITLE, 2);
    }

    private void handleTitle(WritableSheet sheet, String contents) throws WriteException {
        Label label = new Label(0, 0, contents);
        WritableCellFormat wc = new WritableCellFormat();
        wc.setVerticalAlignment(VerticalAlignment.TOP);
        wc.setWrap(true);
        label.setCellFormat(wc);
        sheet.addCell(label);
        sheet.mergeCells(0, 0, getAllLen() - 1, 0);
        sheet.setRowView(0, 3000);
    }

    private void hendleSingleCol(WritableSheet sheet, String title, int len) throws WriteException {
        int startR = START_ROW;
        int startC = START_COL + BASES_INFO.length + ORIGIN_INFO.length + getSpecsDetailLen() + getPriceLen() + STOCK_WARN_INFO.length + len;
        // 需要的长度
        int cl = 1;
        int rl = 3;
        WritableCellFormat title1CellFormat = getTitle1CellFormat();
        Label label = new Label(startC, startR, title);
        label.setCellFormat(title1CellFormat);
        sheet.addCell(label);
        sheet.mergeCells(startC, startR, startC, startR + rl - 1);
        sheet.setColumnView(startC, CELL_WIDTH);
    }

    private int getPriceLen() {
        return getUnitLen() * PRICE_INFO.length;
    }

    private int getSpecsDetailLen() {
        return this.specsQty + this.unitQty * 2;
    }

    private int getUnitLen() {
        return this.unitQty;
    }

    private int getSpecsLen() {
        return this.specsQty;
    }

    private int getAllLen() {
        return BASES_INFO.length + ORIGIN_INFO.length + getSpecsDetailLen() + getPriceLen() + STOCK_WARN_INFO.length + 3;
    }

    private WritableCellFormat getTitle1CellFormat() throws WriteException {
        return this.excleCellFormatGenerater.getBlackCellFormat();
    }

    private WritableCellFormat getTitle2CellFormat() throws WriteException {
        return this.excleCellFormatGenerater.getGrey50CellFormat();
    }

    private WritableCellFormat getTitle3CellFormat() throws WriteException {
        return this.excleCellFormatGenerater.getGrey25CellFormat();
    }
}
