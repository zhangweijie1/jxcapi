package com.mrguo.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author mrguo
 */
public class DateUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String YEAR_PATTERN = "yyyy";
    public static final String MINUTE_ONLY_PATTERN = "mm";
    public static final String HOUR_ONLY_PATTERN = "HH";
    public static final String DATE_TIME_PATTERN_T = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 把日期对象根据生成指定格式的字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            result = sdf.format(date);
        }
        return result;
    }


    /**
     * 日期相加减天数
     * @param date 如果为Null，则为当前时间
     * @param days 加减天数
     * @param includeTime 是否包括时分秒,true表示包含
     * @return
     * @throws ParseException
     */
    public static Date dateAdd(Date date, int days, boolean includeTime) throws ParseException {
        if(date == null){
            date = new Date();
        }
        if(!includeTime){
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            date = sdf.parse(sdf.format(date));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }


    /**
     * 把日期字符串生成指定格式的日期对象
     *
     * @param str
     * @param format
     * @return
     * @throws Exception
     */
    public static Date formatString(String str, String format) throws Exception {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    /**
     * 生成当前年月日字符串
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }


    /**
     * 获取指定范围内的日期集合
     *
     * @param before
     * @param end
     * @return
     * @throws Exception
     */
    public static List<String> getRangeDates(String before, String end) throws Exception {
        List<String> datas = new ArrayList<String>();
        Calendar cb = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();
        cb.setTime(formatString(before, "yyyy-MM-dd"));
        ce.setTime(formatString(end, "yyyy-MM-dd"));
        datas.add(formatDate(cb.getTime(), "yyyy-MM-dd"));
        while (cb.before(ce)) {
            cb.add(Calendar.DAY_OF_MONTH, 1);
            datas.add(formatDate(cb.getTime(), "yyyy-MM-dd"));
        }
        return datas;
    }

    /**
     * 获取指定范围内的月份集合
     *
     * @param before
     * @param end
     * @return
     * @throws Exception
     */
    public static List<String> getRangeMonth(String before, String end) throws Exception {
        List<String> months = new ArrayList<String>();
        Calendar cb = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();
        cb.setTime(formatString(before, "yyyy-MM"));
        ce.setTime(formatString(end, "yyyy-MM"));
        months.add(formatDate(cb.getTime(), "yyyy-MM"));
        while (cb.before(ce)) {
            cb.add(Calendar.MONTH, 1);
            months.add(formatDate(cb.getTime(), "yyyy-MM"));
        }
        return months;
    }

    /**
     * 获取几分钟之前的时间
     *
     * @param m
     * @return
     */
    public static String getMinusMin(int m) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        //设置参数时间
        c.setTime(date);
        //把日期往后增加SECOND 秒.整数往后推,负数往前移动
        c.add(Calendar.SECOND, -60 * m);
        //这个时间就是日期往后推一天的结果
        date = c.getTime();
        String str = df.format(date);
        return str;
    }
}
