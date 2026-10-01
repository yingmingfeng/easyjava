package com.easyjava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yingmingfeng
 * @classname DateUtils
 * @description 时间处理方法
 * @date 2026/9/21 17:19
 */
public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYYMMDD_HH_MM = "yyyy/MM/dd HH:mm";
    public static final String YYYYMMDD = "yyyy/MM/dd";

    public static String format(Date date, String patten) {
        return new SimpleDateFormat(patten).format(date);
    }

    public static Date parse(String date, String patten) {
        try {
            new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
