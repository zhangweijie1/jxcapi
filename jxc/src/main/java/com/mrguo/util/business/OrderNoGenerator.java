package com.mrguo.util.business;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.DateUtil;
import com.mrguo.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName OrderNoGenerator
 * @Description 编号生成器
 * 诉求：
 * 1.前端显示编号，这时候不能真的给redis新增
 * 2.后端, 真的新增的时候获取一个编号，redis真新增
 * 利用时间+类别作为key，记录某类别的编号一天的数量，从而达到计数的作用
 * @date 2020/6/13 9:14 AM
 * @updater 郭成兴
 * @updatedate 2020/6/13 9:14 AM
 */
@Component
public class OrderNoGenerator {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private HttpServletRequest request;

    private static int SUFFIX_LEN = 4;
    // 过期时间，单位秒（1天）
    private static int TTL = 86400;

    //日期格式
    public static final String dateFormat_yyMMdd = "yyMMdd";
    public static final String dateFormat_yyyyMMdd = "yyyyMMdd";
    public static final String dateFormat_yyMM = "yyMM";


    /**
     * 获取票据单号
     *
     * @return
     */
    public String getBillNo(String prefix, int suffixLen) {
        // 编号类型：单据bill
        String orderCat = "BILL_NO:";
        return getLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    /**
     * 生成一个单据号
     *
     * @param prefix    前缀字母：如XSD202002120002，XSD
     * @param suffixLen 后缀长度一般定义为4
     * @return
     */
    public String genBillNo(String prefix, int suffixLen) {
        // 编号类型：单据bill
        String orderCat = "BILL_NO:";
        return genLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String getCustomerCode(String prefix, int suffixLen) {
        String orderCat = "CUSTOMER:";
        return getLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String genCustomerCode(String prefix, int suffixLen) {
        String orderCat = "CUSTOMER:";
        return genLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String getSupplierCode(String prefix, int suffixLen) {
        String orderCat = "SUPPLIER:";
        return getLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String genSupplierCode(String prefix, int suffixLen) {
        String orderCat = "SUPPLIER:";
        return genLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String getSkuCode(String prefix, int suffixLen) {
        // 编号类型：sku
        String orderCat = "SKU:";
        return getLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    public String genSkuCode(String prefix, int suffixLen) {
        // 编号类型：sku
        String orderCat = "SKU:";
        return genLastOrderNo(orderCat, prefix, suffixLen, dateFormat_yyyyMMdd);
    }

    /**
     * 生成最新的编号
     * <p>
     * 单个获取，支持高并发，不建议大批量时循环获取的方式
     *
     * @param prefix     单据号前缀：如出库单前缀EO
     * @param suffixLen  单据号后缀，长度（4则以0000为基础）
     * @param dateFormat 单据号中间日期显示格式
     * @return 订单号
     */
    private String genLastOrderNo(String orderCat, String prefix, int suffixLen, String dateFormat) {
        //先判断日期格式是否为空，为空说明不需要日期
        String time = DateUtil.formatDate(new Date(), dateFormat);
        String key = orderCat + prefix + time + ":";
        Long newNo = redisClient.incr(key, TTL);
        //最新序号
        String nowSeq = String.format("%0" + suffixLen + "d", newNo);
        return prefix + time + nowSeq;
    }

    /**
     * 获取最新编号
     *
     * @param orderCat
     * @param prefix
     * @param suffixLen
     * @param dateFormat
     * @return
     */
    private String getLastOrderNo(String orderCat, String prefix, int suffixLen, String dateFormat) {
        //先判断日期格式是否为空，为空说明不需要日期
        String time = DateUtil.formatDate(new Date(), dateFormat);
        String key = orderCat + prefix + time + ":";
        String s = redisClient.get(key);
        Long newNo = s == null ? Long.valueOf(0) : Long.valueOf(s);
        //最新序号
        String nowSeq = String.format("%0" + suffixLen + "d", newNo + 1);
        return prefix + time + nowSeq;
    }
}
