package com.easyjava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yingmingfeng
 * @classname PropertiesUtils
 * @description 获取数据库属性
 * @date 2026/9/20 10:25
 */
public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String,String> PROPER_MAP = new ConcurrentHashMap<>();

    static {
        InputStream is = null;
        try{
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                PROPER_MAP.put(key, props.getProperty(key));
            }
        }catch (Exception e){
            if(is !=null){
                try{
                    is.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }

    public static String getString(String key){
        return PROPER_MAP.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getString("db.url"));
    }
}
