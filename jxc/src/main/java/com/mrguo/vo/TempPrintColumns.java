package com.mrguo.vo;

import lombok.Data;

/**
 * @ClassName: TempPrintColumns
 * @Description: 单据打印的列数据
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 9:12 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
public class TempPrintColumns {

    private String field;

    private String title;

    private String width;
}
