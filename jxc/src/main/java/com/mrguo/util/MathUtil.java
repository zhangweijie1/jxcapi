package com.mrguo.util;

/**
 * 数学工具类
 *
 * @author Administrator
 */
public class MathUtil {

    /**
     * 格式化数字 保留两位
     *
     * @param n
     * @return
     */
    public static float format2Bit(float n) {
        return (float) (Math.round(n * 100)) / 100;
    }

    public static String stringAddOne(String testStr) {
        //根据不是数字的字符拆分字符串
        String[] strs = testStr.split("[^0-9]");
        //取出最后一组数字
        String numStr = strs[strs.length - 1];
        //如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
        if (numStr != null && numStr.length() > 0) {
            //取出字符串的长度
            int n = numStr.length();
            //将该数字加一
            int num = Integer.parseInt(numStr) + 1;
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            throw new NumberFormatException();
        }
    }
}
