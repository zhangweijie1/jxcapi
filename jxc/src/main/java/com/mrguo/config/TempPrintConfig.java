package com.mrguo.config;

import com.mrguo.vo.TempPrintVo;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/43:58 PM
 * @updater 郭成兴
 * @updatedate 2020/6/43:58 PM
 */

@Data
@Configuration
@PropertySource(encoding = "UTF-8", value = "classpath:application-print.properties")
public class TempPrintConfig {

    private TempPrintVo saleOrder;
    private TempPrintVo sale;
    private TempPrintVo saleReturn;
    private TempPrintVo purchaseOrder;
    private TempPrintVo purchase;
    private TempPrintVo purchaseReturn;
    private TempPrintVo inventory;
    private TempPrintVo dispatch;
    private TempPrintVo stockin;
    private TempPrintVo stockout;
    private TempPrintVo borrowin;
    private TempPrintVo borrowout;
    private TempPrintVo assembel;
}
