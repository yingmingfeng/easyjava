package com.easyjava.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author yingmingfeng
 * @classname JsonUtils
 * @description json方法
 * @date 2026/9/21 14:03
 */
public class JsonUtils {
    public static String convertToJson(Object obj){
        if(null == obj){
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }
}
