package com.mrguo.vo;

import lombok.Data;

/**
 * @ClassName: TempPrintMaster
 * @Description: 打印的单据头实体
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 9:11 上午
 * @Copyright 南通市韶光科技有限公司
 **/
@Data
public class TempPrintMaster {

    /**
     * 字段
     */
    private String field;

    /**
     * 标题
     */
    private String title;

    /**
     * 字段值
     */
    private String value;

    /**
     * 位置
     */
    private String pos;
}
