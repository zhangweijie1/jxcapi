package com.mrguo.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormat;

public class ExcelCreate {

    public HSSFWorkbook wb = null;

    public HSSFSheet sheet = null;

    public HSSFDataFormat format = null;

    public HSSFRow hdRow = null;

    int listlength = 0;

    /**
     * 设置工作表的格式
     *
     * @param sheetName
     */
    public ExcelCreate() {
        wb = new HSSFWorkbook();
    }

    public void createSheet(String sheetName) {
        sheet = wb.createSheet(sheetName);
        format = wb.createDataFormat();
        hdRow = sheet.createRow(0);
        sheet.setDefaultRowHeightInPoints(120);
        sheet.setDefaultColumnWidth(12);
    }

    /**
     * 设置各列单元格宽度
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/7/25 9:00 AM
     * @updater 郭成兴
     * @updatedate 2020/7/25 9:00 AM
     */
    public void setDefaultCellHighWidthInRange(short[] eachCellWidth, int high) {
        // 假定第一行和第一行所需的单元个已经建立好了，也就是说，在这之前已经调用了DesignXlsHeaderFooter.setXlsHeader
        // 设置默认高
        sheet.setDefaultRowHeightInPoints(high);
        /* 设置各列单元格宽度 */
        for (int i = 0; i < eachCellWidth.length; i++) {
             System.out.print(""+i+"\t");
            sheet.setColumnWidth((short) i, (short) ((eachCellWidth[i]) * 256));
        }
    }

    /**
     * 表头数据
     *
     * @throws Exception
     */
    public void addHeader(List rowvalues, boolean isFilter) throws Exception {
        listlength = rowvalues.size();
        // 设置字体
        HSSFFont workFont = wb.createFont();
        workFont.setFontName("微软雅黑");
        workFont.setFontHeightInPoints((short) 14);
        workFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 表头样式及背景色
        HSSFCellStyle hdStyle = wb.createCellStyle();
        hdStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        hdStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        hdStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        hdStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        hdStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        hdStyle.setRightBorderColor(HSSFColor.BLACK.index);
        hdStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hdStyle.setTopBorderColor(HSSFColor.BLACK.index);
        hdStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hdStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        hdStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        hdStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        hdStyle.setFont(workFont);

        String[] title = new String[rowvalues.size()];
        for (int i = 0; i < rowvalues.size(); i++) {
            title[i] = (String) rowvalues.get(i);
        }
        HSSFRow dtRow = sheet.createRow((1));
        if (isFilter == true) {
            for (int i = 0; i < title.length; i++) {
                HSSFCell cell1 = hdRow.createCell(i);
                HSSFRichTextString value = new HSSFRichTextString(title[i]);
                cell1.setCellValue(value);
                cell1.setCellStyle(hdStyle);
            }
        } else {
            for (int i = 0; i < title.length; i++) {
                HSSFCell cell2 = dtRow.createCell(i);
                HSSFRichTextString value2 = new HSSFRichTextString(title[i]);
                cell2.setCellValue(value2);
            }
        }
    }

    /**
     * 数据的导入
     */
    // public void addRow(HashMap<Integer, List> rowvalues) {
    // for (int i = 0; i < rowvalues.size(); i++) {
    // HSSFRow dtRow = sheet.createRow((i + 2));
    // List list = (List) rowvalues.get(i);
    // for (int j = 0; j < list.size(); j++) {
    // Object cell_data = list.get(j);
    // HSSFCell cell = dtRow.createCell(j);
    // if (cell_data instanceof String) {
    // cell.setCellValue(new HSSFRichTextString((String) cell_data));
    // } else if (cell_data instanceof Double) {
    // HSSFCellStyle dtStyle = wb.createCellStyle();
    // dtStyle.setDataFormat(format.getFormat("yyyy/MM/dd"));
    // cell.setCellValue((Double) cell_data);
    // } else if (cell_data instanceof Integer) {
    // cell.setCellValue(Double.valueOf(String.valueOf(cell_data)));
    // } else if (cell_data instanceof Date) {
    // cell.setCellValue((Date) cell_data);
    // } else if (cell_data instanceof Boolean) {
    // cell.setCellValue((Boolean) cell_data);
    // }
    // // 正文格式
    // HSSFCellStyle dtStyle = wb.createCellStyle();
    // dtStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    // dtStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    // dtStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    // dtStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    // dtStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    // dtStyle.setRightBorderColor(HSSFColor.BLACK.index);
    // dtStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    // dtStyle.setTopBorderColor(HSSFColor.BLACK.index);
    // dtStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
    // //背景颜色
    // if(i%2!=0)
    // dtStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    // dtStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    // cell.setCellStyle(dtStyle);
    // }
    // }
    //
    // }
    /**
     * 添加一行
     */
    int s = 1;

    public void addRow(List rowvalues) {
        HSSFRow dtRow = sheet.createRow(s++);
        DataFormat format = wb.createDataFormat();

        HSSFCellStyle dtStyle = wb.createCellStyle();
        dtStyle.setDataFormat(format.getFormat("text"));
        dtStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dtStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        dtStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dtStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        dtStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dtStyle.setRightBorderColor(HSSFColor.BLACK.index);
        dtStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dtStyle.setTopBorderColor(HSSFColor.BLACK.index);
        dtStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

        HSSFCellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("yyyy-m-d"));
        dateStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dateStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        dateStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dateStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        dateStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dateStyle.setRightBorderColor(HSSFColor.BLACK.index);
        dateStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dateStyle.setTopBorderColor(HSSFColor.BLACK.index);
        dateStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

        for (int j = 0; j < rowvalues.size(); j++) {
            String flag = "";
            Object cell_data = rowvalues.get(j);
            HSSFCell cell = dtRow.createCell(j);
            // 正文格式
            if (cell_data instanceof String) {
                flag = "string";
                cell.setCellValue((String) cell_data);
            } else if (cell_data instanceof Double) {
                cell.setCellValue((Double) cell_data);
            } else if (cell_data instanceof Integer) {
                cell.setCellValue(Double.valueOf(String.valueOf(cell_data)));
            } else if (cell_data instanceof Date) {
                flag = "date";
                cell.setCellValue((Date) cell_data);
            } else if (cell_data instanceof Boolean) {
                cell.setCellValue((Boolean) cell_data);
            } else if (cell_data instanceof Float) {
                cell.setCellValue((Float) cell_data);
            }
            // 背景颜色
//			if(s%2!=0)
//			dtStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//			dtStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            if (StringUtils.isBlank(flag) || "string".equals(flag)) {
                cell.setCellStyle(dtStyle);
            } else if ("date".equals(flag)) {
                cell.setCellStyle(dateStyle);
            }

        }
        // }
    }
    /**
     * 添加相同的行
     * @param starRow
     * @param rows
     */
//	public  void insertRow(int starRow,int rows) {
//
//		  sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows,true,false);
////		  Parameters:
////		   startRow - the row to start shifting
////		   endRow - the row to end shifting
////		   n - the number of rows to shift
////		   copyRowHeight - whether to copy the row height during the shift
////		   resetOriginalRowHeight - whether to set the original row's height to the default
//		  
//		  starRow = starRow - 1;
//
//		  HSSFRow sourceRow = null;
//		  HSSFRow targetRow = null;
//		  HSSFCell sourceCell = null;
//		  HSSFCell targetCell = null;
//		  for (int i = 0; i < rows; i++) {
//
//		   short m;
//
//		   starRow = starRow + 1;
//		   sourceRow = sheet.getRow(starRow);
//		   targetRow = sheet.createRow(starRow + 1);
//		   targetRow.setHeight(sourceRow.getHeight());
//
//		   for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {
//
//		    sourceCell = sourceRow.getCell(m);
//		    targetCell = targetRow.createCell(m);
//
//		    //targetCell.setEncoding(sourceCell.getEncoding());
//		    targetCell.setCellStyle(sourceCell.getCellStyle());
//		    targetCell.setCellType(sourceCell.getCellType());
//
//		   }
//		  }
//
//		 } 

    /**
     * 给指定的行追加一行数据
     *
     * @param rowvalues
     * @param row
     */
    public void insertRow(List rowvalues, int row) {
        sheet.shiftRows(row, sheet.getLastRowNum(), 1);
        HSSFRow dtRow = sheet.createRow(row);
        for (int j = 0; j < rowvalues.size(); j++) {
            Object cell_data = rowvalues.get(j);
            HSSFCellStyle dtStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            HSSFCell cell = dtRow.createCell(j);
            if (cell_data instanceof String) {
                dtStyle.setDataFormat(format.getFormat("text"));
                cell.setCellValue((String) cell_data);
            } else if (cell_data instanceof Double) {
                cell.setCellValue((Double) cell_data);
            } else if (cell_data instanceof Integer) {
                cell.setCellValue(Double.valueOf(String.valueOf(cell_data)));
            } else if (cell_data instanceof Date) {
                dtStyle.setDataFormat(format.getFormat("yyyy-m-d"));
                cell.setCellValue((Date) cell_data);
            } else if (cell_data instanceof Boolean) {
                cell.setCellValue((Boolean) cell_data);
            } else if (cell_data instanceof Float) {
                cell.setCellValue((Float) cell_data);
            }
            dtStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            dtStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            dtStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            dtStyle.setLeftBorderColor(HSSFColor.BLACK.index);
            dtStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            dtStyle.setRightBorderColor(HSSFColor.BLACK.index);
            dtStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            dtStyle.setTopBorderColor(HSSFColor.BLACK.index);
            dtStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            // 背景颜色
//			 if(s%2!=0)
//			dtStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//			dtStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cell.setCellStyle(dtStyle);
        }
    }

    /**
     * 删除指定的行
     *
     * @param row
     */
    public void delRow(int row) {
        if (row > 0) {
            try {
                //HSSFRow dtRow = sheet.getRow(row);
                sheet.shiftRows(row, sheet.getLastRowNum(), -1);
                //sheet.removeRow(dtRow);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("error");
            }
        } else {
            System.out.println("错误的");
        }
    }

    /**
     * 给指定的列给出下拉列表
     *
     * @param row
     * @param cells
     * @param list
     */
    public void setSelect(int row, int cells, List cellvalue) {
        String[] str = new String[cellvalue.size()];
        for (int i = 0; i < cellvalue.size(); i++) {
            str[i] = (String) cellvalue.get(i);
        }
        CellRangeAddressList regions = new CellRangeAddressList(row, 65535, cells, cells);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(str);
        HSSFDataValidation dataValidate = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidate); // 加入数据有效性到当前sheet对象
    }

    /**
     * 合并单元格//左上角到右下角int col1,int row1,int col2,int row2
     */
    // public void hebing(){
    // HSSFRow row = sheet.createRow(0);
    // HSSFCell cell = row.createCell(0);
    // cell.setCellValue(sheetName);
    // sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 5));
    // }

    /**
     * 具体文件生成的路径
     *
     * @param file
     * @throws Exception
     */
    public void exportExcel(String file) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 具体文件生成的文件
     *
     * @param file
     * @throws Exception
     */
    public void exportExcel(File file) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 具体文件生成的文件
     *
     * @param file
     * @throws Exception
     */
    public void exportExcel(OutputStream outputstream) throws Exception {
        BufferedOutputStream buffout = new BufferedOutputStream(outputstream);
        wb.write(buffout);
        buffout.flush();
        buffout.close();
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("员工号");
        list.add("姓名");
        list.add("出生日前");
        list.add("工作地点");
        list.add("职务");
        list.add("性别");
        list.add("那裡");

        ExcelCreate s = new ExcelCreate();
        s.createSheet("系统报名表");
        s.addHeader(list, true);
        s.addHeader(list, false);

        List lists = null;
        for (int i = 0; i < 6; i++) {

            lists = new ArrayList();
            lists.add("A000" + i);
            if (i == 1) {
                lists.add("赵云");
            } else if (i == 2) {
                lists.add("关羽");
            } else if (i == 3) {
                lists.add("张飞");
            } else if (i == 4) {
                lists.add("什么");
            } else if (i == 5) {
                lists.add("那个");
            }
            lists.add("6500444444444444444444444444444444 ");
//			new SimpleDateFormat("yyyy-M-d").format(new Date())
            lists.add(new Date());
            lists.add("生死战" + i);
            lists.add("男" + i);
            lists.add("常山的" + i);
            s.addRow(lists);
        }
        List lists1 = new ArrayList();
        lists1.add("五虎上将之一");
        lists1.add("赵云");
        lists1.add("6500");
        lists1.add("2010-9-1");
        lists1.add("生死战");
        lists1.add("男sdfdsf");
        lists1.add("weher");


        s.insertRow(lists1, 3);
        //s.insertRow(5, 6);
        //s.delRow(5);
        s.setSelect(3, 3, list);
        s.createSheet("第二张系统报名表");// 第二张工作表

        File file = new File("E:\\ss.xls");
        s.exportExcel(file);
    }
}
