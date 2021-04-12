package com.mrguo.dto.report;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/5/125:29 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:29 PM
 */

@Data
public class PurchaseReportParam {

    private String storeId;

    /**
     * 起始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private String beginTime;

    /**
     * 终止时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private String endTime;

    /**
     * 某时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private String time;

    private String skuId;

    private String catId;

    private String comegoId;

    private String keywords;
}
