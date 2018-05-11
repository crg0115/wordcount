package com.nk.wordcount.util;

/**
 * 字符串工具类 from apache
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空 from apache
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空 from apache
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
