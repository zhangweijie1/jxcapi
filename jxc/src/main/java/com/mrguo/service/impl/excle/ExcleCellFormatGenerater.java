package com.mrguo.service.impl.excle;

import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

/**
 * @author 郭成兴
 * @ClassName ExcleCellFormatGenerater
 * @Description excle，cell的样式
 * @date 2020/7/317:55 AM
 * @updater 郭成兴
 * @updatedate 2020/7/317:55 AM
 */
public class ExcleCellFormatGenerater {

    private WritableCellFormat blackCellFormat;
    private WritableCellFormat grey25CellFormat;
    private WritableCellFormat grey50CellFormat;

    public WritableCellFormat getBlackCellFormat() throws WriteException {
        if (this.blackCellFormat == null) {
            WritableCellFormat wc = new WritableCellFormat();
            WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.WHITE);
            wc.setBackground(jxl.format.Colour.GREY_80_PERCENT);
            wc.setFont(font);
            wc.setAlignment(jxl.format.Alignment.CENTRE);
            wc.setVerticalAlignment(VerticalAlignment.CENTRE);
            this.blackCellFormat = wc;
            return wc;
        } else {
            return this.blackCellFormat;
        }
    }

    public WritableCellFormat getBlackCellFormat(WritableFont font) throws WriteException {
        if (this.blackCellFormat == null) {
            WritableCellFormat wc = new WritableCellFormat();
            wc.setBackground(jxl.format.Colour.GREY_80_PERCENT);
            wc.setFont(font);
            wc.setAlignment(jxl.format.Alignment.CENTRE);
            wc.setVerticalAlignment(VerticalAlignment.CENTRE);
            this.blackCellFormat = wc;
            return wc;
        } else {
            return this.blackCellFormat;
        }
    }

    public WritableCellFormat getGrey25CellFormat() throws WriteException {
        if (this.grey25CellFormat == null) {
            WritableCellFormat wc = new WritableCellFormat();
            wc.setBackground(jxl.format.Colour.GREY_25_PERCENT);
            wc.setAlignment(jxl.format.Alignment.CENTRE);
            wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            this.grey25CellFormat = wc;
            return wc;
        } else {
            return this.grey25CellFormat;
        }
    }

    public WritableCellFormat getGrey50CellFormat() throws WriteException {
        if (this.grey50CellFormat == null) {
            WritableCellFormat wc = new WritableCellFormat();
            wc.setBackground(jxl.format.Colour.GREY_50_PERCENT);
            wc.setAlignment(jxl.format.Alignment.CENTRE);
            wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            this.grey50CellFormat = wc;
            return wc;
        } else {
            return this.grey50CellFormat;
        }

    }
}
