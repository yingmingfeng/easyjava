package com.easyjava.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingmingfeng
 * @classname TableInfo
 * @description 表信息
 * @date 2026/9/20 19:07
 */
@Setter
@Getter
public class TableInfo {
    /**
     * 表名:tb_product_info
     */
    private String tableName;
    /**
     * bean名称:ProductInfo
     */
    private String beanName;
    /**
     * 另一种bean名称:ProductInfoQuery
     */
    private String beanParamName;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 字段信息
     */
    private List<FieldInfo> fieldList;
    /**
     * 字段拓展信息（fuzzy、timeStart、timeEnd）
     */
    private List<FieldInfo> fieldExtendList;
    /**
     * 唯一索引集合（LinkedHashMap确保索引按照`声明顺序`生成）
     */
    private Map<String, List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();
    /**
     * 是否有date类型
     */
    private Boolean haveDate;
    /**
     * 是否有时间类型
     */
    private Boolean haveDateTime;
    /**
     * 是否有 bigdecimal 类型
     */
    private Boolean haveBigDecimal;
}
