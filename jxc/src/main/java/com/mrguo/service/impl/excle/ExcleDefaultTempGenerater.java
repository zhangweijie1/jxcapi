package com.mrguo.service.impl.excle;

import com.mrguo.interfaces.ExcleName;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.poi.ss.formula.functions.T;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName ExcleDefaultTempGenerater
 * @Description excle默认模板生成
 * @date 2020/7/308:11 PM
 * @updater 郭成兴
 * @updatedate 2020/7/308:11 PM
 */
public class ExcleDefaultTempGenerater {

    /**
     * cell格式
     */
    private ExcleCellFormatGenerater cellFormatGenerater;

    /**
     * excle数据实体的Class
     */
    private Class cls;

    /**
     * 第一行说明文字
     */
    private String headerContents;

    public ExcleDefaultTempGenerater(Class cls,
                                     String headerContents) {
        this.cellFormatGenerater = new ExcleCellFormatGenerater();
        this.cls = cls;
        this.headerContents = headerContents;
    }

    /**
     * 生成一个空excle模板
     *
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public ByteArrayOutputStream createExcleTemp(String sheetName) throws IOException, WriteException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableWorkbook writableSheet = createWritableWorkbook(os, sheetName);
        writableSheet.write();
        writableSheet.close();
        return os;
    }

    /**
     * 创建一个只有title的WritableSheet
     *
     * @param os
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public WritableWorkbook createWritableWorkbook(ByteArrayOutputStream os,
                                                   String sheetName) throws IOException, WriteException {
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet sheet = wwb.createSheet(sheetName, 0);
        Field[] fields = this.cls.getDeclaredFields();
        int rowStart = 1;
        for (int j = 0; j < fields.length; j++) {
            ExcleName annotation = fields[j].getAnnotation(ExcleName.class);
            String titleName = annotation.value();
            Label label = new Label(j, rowStart, titleName);
            WritableFont font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.WHITE);
            WritableCellFormat blackCellFormat = this.cellFormatGenerater.getBlackCellFormat(font);
            label.setCellFormat(blackCellFormat);
            sheet.addCell(label);
            sheet.setRowView(rowStart, 400);
            sheet.setColumnView(j, 14);
        }
        // handle header
        Label label = new Label(0, 0, this.headerContents);
        WritableCellFormat wc = new WritableCellFormat();
        wc.setVerticalAlignment(VerticalAlignment.TOP);
        wc.setWrap(true);
        label.setCellFormat(wc);
        sheet.addCell(label);
        sheet.mergeCells(0, 0, fields.length - 1, 0);
        sheet.setRowView(0, 2000);
        return wwb;
    }

    /**
     * 数据转化成excle
     *
     * @param listData
     * @param sheetName
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public <E> ByteArrayOutputStream listData2Excle(List<E> listData, String sheetName) throws IOException, WriteException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableWorkbook wwb = createWritableWorkbook(os, sheetName);
        WritableSheet sheet = wwb.getSheet(0);
        int startRow = 2;
        int indexRow = startRow;
        for (E t : listData) {
            Class<?> cls = t.getClass();
            Field[] listDataFileds = cls.getDeclaredFields();
            for (int i = 0; i < listDataFileds.length; i++) {
                Field f = listDataFileds[i];
                f.setAccessible(true);
                String value = null;
                try {
                    value = f.get(t) == null ? "" : String.valueOf(f.get(t));
                    String key = f.getName();
                    if ("isEnable".equals(key)) {
                        value = "1".equals(value) ? "启用" : "停用";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Label label = new Label(i, indexRow, value);
                sheet.addCell(label);
            }
            indexRow++;
        }
        wwb.write();
        wwb.close();
        return os;
    }
}
