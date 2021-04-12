package com.mrguo.dto.charts;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/2610:04 AM
 * @updater 郭成兴
 * @updatedate 2020/5/2610:04 AM
 */

@Data
public class Chart {

    @Column(name = "amount_payable")
    private BigDecimal amountPayable;

    @Column(name = "bill_count")
    private int billCount;

    /**
     * 日期
     */
    private String time;

    @Column(name = "begin_time")
    private String beginTime;

    @Column(name = "end_time")
    private String endTime;
}
