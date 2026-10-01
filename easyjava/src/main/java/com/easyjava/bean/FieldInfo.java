package com.easyjava.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yingmingfeng
 * @classname FieldInfo
 * @description 表字段信息
 * @date 2026/9/20 19:13
 */
@Setter
@Getter
public class FieldInfo {
    /**
     * 字段名称
     * FieldName : company_id
     */
    private String FieldName;

    /**
     * bean属性名称
     * propertyName : company_id => companyId
     * sqlType : sql中是什么类型 varchar(30)
     */
    private String propertyName;
    private String sqlType;

    /**
     * 字段类型
     * javaType : sqlType在java中对应的类型
     */
    private String javaType;

    /**
     * 字段备注
     */
    private String comment;

    /**
     * 字段是否自增长
     */
    private Boolean isAutoIncrement;
}
