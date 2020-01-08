package io.itman.library.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describe
 *
 * @author Gao
 * @date 2018/12/5
 */
public class StringUtil {


    public static  final String  DEFAULT_SPLIT=",";

    public static  final  int  DEFAULT_SORT=99999;

    /**
     * 判断String是否为空
     *
     */
    public static boolean isEmptyString(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 只判断多个String是否为空(无论有没全角或半角的空格) 若非空则返回true,否则返回false
     *
     * @param str
     * @return boolean
     */
    public static boolean isEmptyAllString(String... str) {
        if (null == str) {
            return true;
        }
        for (String s : str) {
            if (isEmptyString(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * String -> int(默认值为0)
     *
     * @param string
     * @return int
     */
    public static int parseInt(String string) {

        return parseInt(string,0);
    }


    /**
     * String -> int
     *
     * @param string
     * @return int
     */
    public static int parseInt(String string, int def) {
        if(isEmptyString(string)){
            return def;
        }
        int num = def;
        try {
            num = Integer.parseInt(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }



    /**
     * String -> short(默认值为0)
     *
     * @param string
     * @return int
     */
    public static short parseShort(String string) {
        return parseShort(string, (short) 0);
    }

    /**
     * String -> short
     *
     * @param string
     * @return int
     */
    public static short parseShort(String string, short def) {
        if (isEmptyString(string)) {
            return def;
        }
        short num = def;
        try {
            num = Short.parseShort(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }


    /**
     * String -> long  (默认值为0)
     *
     * @param string
     * @return long
     */
    public static long parseLong(String string) {

        return parseLong(string,0L);
    }

    /**
     * String -> long
     *
     * @param string
     * @return long
     */
    public static long parseLong(String string, long def) {
        if (isEmptyString(string)) {
            return def;
        }
        long num;
        try {
            num = Long.parseLong(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }


    /**
     * String -> double  (默认值为0)
     *
     * @param string
     * @return long
     */
    public static double parseDouble(String string) {

        return parseDouble(string,0);
    }

    /**
     * String -> double
     *
     * @param string
     * @return long
     */
    public static double parseDouble(String string, double def) {
        if (isEmptyString(string))
            return def;
        double num;
        try {
            num = Double.parseDouble(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }


    /**
     * String -> float (默认值为0)
     *
     * @param string
     * @return long
     */
    public static float parseFloat(String string) {

        return parseFloat(string,0);
    }

    /**
     * String -> float
     *
     * @param string
     * @return long
     */
    public static float parseFloat(String string, float def) {
        if (isEmptyString(string))
            return def;
        float num;
        try {
            num = Float.parseFloat(string);
        } catch (Exception e) {
            num = def;
        }
        return num;
    }

    /**
     * String -> float
     * @param string
     * @param def  默认值
     * @param digit 保留位数
     * @return
     */
    public static float parseFloat(String string, float def, int digit) {
        if (isEmptyString(string))
            return def;
        float num;
        try {
            num = Float.parseFloat(string);
        } catch (Exception e) {
            num = def;
        }
        if (digit > 0 && num != def) {
            BigDecimal bigDecimal = new BigDecimal(num);
            float twoDecimalP = bigDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
            return twoDecimalP;
        } else {
            return num;
        }
    }


    /**
     * 过滤字符串中的可能存在XSS攻击的非法字符
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll(" ", "");
        return value;
    }

    /**
     * 去除HTML标签
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr){
        //定义script的正则表达式
        String regExScript="<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式
        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式
        String regExHtml="<[^>]+>";

        Pattern p_script=Pattern.compile(regExScript,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        //过滤script标签
        htmlStr=m_script.replaceAll("");

        Pattern p_style=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        //过滤style标签
        htmlStr=m_style.replaceAll("");

        Pattern p_html=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        //过滤html标签
        htmlStr=m_html.replaceAll("");
        //返回文本字符串
        return htmlStr.trim();
    }

    /**
     *去除字符串中的特殊字符
     * @param str
     * @return
     */
    public static String stringFilter(String str){
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
