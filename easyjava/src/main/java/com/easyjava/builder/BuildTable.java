package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.JsonUtils;
import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingmingfeng
 * @classname BuilderTable
 * @description 读jdbc生成table
 * @date 2026/9/20 11:02
 */
public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;

    // 拿到表及其注释
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static String SQL_SHOW_TABLE_FIELD = "show full fields from %s";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver-class-name");
        String url = PropertiesUtils.getString("db.url");
        String username = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("连接数据库失败", e);
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("Name");
                String tableComment = tableResult.getString("Comment");

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = beanName.substring(beanName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName, true);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(tableComment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);

                logger.info("表: {}", JsonUtils.convertToJson(tableInfo));

                List<FieldInfo> fieldInfoList = readFieldInfo(tableInfo);
                logger.info("字段信息: {}", JsonUtils.convertToJson(fieldInfoList));

                getKeyIndexInfo(tableInfo);
                logger.info("tableInfo: {}", JsonUtils.convertToJson(tableInfo));

                tableInfoList.add(tableInfo);
            }
            return tableInfoList;
        } catch (Exception e) {
            logger.error("getTable 查询表注释失败", e);
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableInfoList;
    }

    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        List<FieldInfo> fieldExtendList = new ArrayList<>();

        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELD, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();

            Boolean haveDateTime = false;
            Boolean haveDate = false;
            Boolean haveBigDecimal = false;

            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");

                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                String propertyName = processFiled(field, false);
                FieldInfo fieldInfo = new FieldInfo();

                fieldInfo.setFieldName(field);
                fieldInfo.setSqlType(type);
                fieldInfo.setComment(comment);
                fieldInfo.setIsAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));

                fieldInfoList.add(fieldInfo);

                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
                    haveDate = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)){
                    haveDateTime = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)){
                    haveBigDecimal = true;
                }
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setSqlType(fieldInfo.getSqlType());
                    fuzzyField.setPropertyName(fieldInfo.getPropertyName() +Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fieldExtendList.add(fuzzyField);
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())||ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setFieldName(fieldInfo.getFieldName());
                    timeStartField.setJavaType("String");
                    timeStartField.setSqlType(fieldInfo.getSqlType());
                    timeStartField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    fieldExtendList.add(timeStartField);

                    FieldInfo timeEndField = new FieldInfo();
                    timeEndField.setFieldName(fieldInfo.getFieldName());
                    timeEndField.setJavaType("String");
                    timeEndField.setSqlType(fieldInfo.getSqlType());
                    timeEndField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    fieldExtendList.add(timeEndField);
                }
            }
            tableInfo.setHaveDate(haveDate);
            tableInfo.setHaveDateTime(haveDateTime);
            tableInfo.setHaveBigDecimal(haveBigDecimal);
            tableInfo.setFieldList(fieldInfoList);
            tableInfo.setFieldExtendList(fieldExtendList);
        } catch (Exception e) {
            logger.error("readFieldInfo 查询表注释失败", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fieldInfoList;
    }

    private static List<FieldInfo> getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        try {
            Map<String,FieldInfo> tempMap = new HashMap<>();
            for(FieldInfo fieldInfo:tableInfo.getFieldList()){
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }

            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");
                if(nonUnique == 1){
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if(keyFieldList == null){
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                keyFieldList.add(tempMap.get(columnName));
            }
        } catch (Exception e) {
            logger.error("getKeyIndexInfo 读取索引失败", e);
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fieldInfoList;
    }

    /**
     * @param filed                字段名
     * @param upperCaseFirstLetter 是否首字母大写
     * @return 处理后的字段名
     * @功能描述: 根据首字母是否大写处理字段名
     */
    private static String processFiled(String filed, Boolean upperCaseFirstLetter) {
        StringBuffer sb = new StringBuffer();
        String[] fields = filed.split("_");
        sb.append(upperCaseFirstLetter ? StringUtils.upperCaseFirstLetter(fields[0]) : fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(StringUtils.upperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别的类型:" + type);
        }
    }
}
