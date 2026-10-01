package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;

import com.easyjava.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yingmingfeng
 * @classname BuildMapperXmll
 * @description 生成xml文件
 * @date 2026/9/23 17:31
 */
@Slf4j
public class BuildMapperXml {
    private static final String BASE_RESULT_MAP = "base_result_map";
    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITIONS = "base_query_condition";
    private static final String BASE_QUERY_CONDITIONS_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITIONS = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, className + ".xml"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            //Mybatis映射文件头部声明
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<!DOCTYPE mapper PUBLIC \"//-mybatis.org//DTD Mapper 3.0//EN\"\n" +
                    "\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">\n");
            bw.newLine();
            //实体映射
            bw.write("\t<!--实体映射-->\n");

            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">\n");

            FieldInfo idField = null;
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())) {
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if (fieldInfoList.size() == 1) {
                        idField = fieldInfoList.get(0);
                        break;
                    }
                }
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t<!-- " + fieldInfo.getComment() + "-->\n");
                String key = "";
                if (idField != null && idField.getPropertyName().equals(fieldInfo.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }

                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\" />");
                bw.newLine();
            }

            bw.write("\t</resultMap>\n\n");

            //通用查询列
            bw.write("\t<!--通用查询列-->\n");
            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">\n");

            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilderStr = columnBuilder.toString().substring(0, columnBuilder.toString().lastIndexOf(","));
            bw.write("\t\t" + columnBuilderStr + "\n");
            bw.write("\t</sql>\n\n");

            //基础查询条件
            bw.write("\t<!--基础查询条件-->\n");
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITIONS + "\">\n");

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                String stringQuery = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
                }

                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery + "\">");
                bw.newLine();
                bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t</if>\n");

            }
            bw.write("\t</sql>\n\n");

            //拓展查询条件
            bw.write("\t<!--拓展查询条件-->\n");
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITIONS_EXTEND + "\">\n");

            for (FieldInfo fieldInfo : tableInfo.getFieldExtendList()) {
                String andWhere = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES,
                        fieldInfo.getSqlType())) {
                    if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                    } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), " + "interval -1 day) ]]>";
                    }
                }

                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
                bw.newLine();
                bw.write("\t\t\t" + andWhere);
                bw.newLine();
                bw.write("\t\t</if>\n");

            }
            bw.write("\t</sql>\n\n");

            //查询条件
            bw.write("\t<!--查询条件-->\n");
            bw.write("\t<sql id=\"" + QUERY_CONDITIONS + "\">\n");
            bw.write("\t\t<where>\n");
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITIONS + "\" />\n");
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITIONS_EXTEND + "\" />\n");
            bw.write("\t\t</where>\n");
            bw.write("\t</sql>\n\n");

            //查询列表
            bw.write("\t<!--查询列表-->\n");
            bw.write("\t<select id=\"" + "selectList" + "\" resultMap=\"" + "base_result_map" + "\">\n");
            bw.write("\t\tSELECT\n");
            bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\" />\n");
            bw.write("\t\tFROM tb_product_info\n");
            bw.write("\t\t<include refid=\"" + QUERY_CONDITIONS + "\" />\n");
            bw.write("\t\t<if test=\"query.orderBy!=null\">order by ${query.orderBy}</if>\n");
            bw.write("\t\t<if test=\"query.simplePage!=null\">limit #{query.simplePage.start},#{query.simplePage.end}</if>\n");
            bw.write("\t</select>\n\n");

            //查询数量
            bw.write("\t<!--查询数量-->\n");
            bw.write("\t<select id=\"" + "selectCount" + "\" resultType=\"" + "java.lang.Integer" + "\">\n");
            bw.write("\t\tSELECT\n");
            bw.write("\t\tcount(1)\n");
            bw.write("\t\tFROM tb_product_info\n");
            bw.write("\t\t<include refid=\"" + QUERY_CONDITIONS + "\" />\n");
            bw.write("\t</select>\n\n");

            //插入（匹配有值的字段）
            bw.write("\t<!--插入（匹配有值的字段）-->\n");
            bw.write("\t<insert id=\"" + "insert" + "\" parameterType=\"" + poClass + "\">\n");

            FieldInfo autoIncrementField = null;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfo.getIsAutoIncrement() != null && fieldInfo.getIsAutoIncrement()) {
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if (autoIncrementField != null) {
                bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementField.getFieldName() + "\" resultType=\"" + autoIncrementField.getJavaType() + "\" order=\"AFTER\">\n");
                bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
                bw.write("\t\t</selectKey>\n");
            }

            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</trim>\n");

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</trim>\n");
            bw.write("\t</insert>\n\n");

            //插入或者更新（匹配有值的字段）
            bw.write("\t<!--插入或者更新（匹配有值的字段）-->\n");
            bw.write("\t<insert id=\"" + "insertOrUpdate" + "\" parameterType=\"" + poClass + "\">\n");

            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</trim>\n");

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</trim>\n");
            bw.write("\t\ton DUPLICATE key update\n");

            Map<String, String> keyTempMap = new HashMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                for (FieldInfo item : fieldInfoList) {
                    keyTempMap.put(item.getFieldName(), item.getFieldName());
                }
            }

            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">\n");
            bw.write("\t\t\t<!--把主键和唯一索引对应的字段从 ON DUPLICATE KEY UPDATE 的更新列表中移除-->\n");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (keyTempMap.get(fieldInfo.getFieldName()) != null) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</trim>\n");
            bw.write("\t</insert>\n\n");

            //批量插入
            bw.write("\t<!--批量插入-->\n");
            bw.write("\t<insert id=\"" + "insertBatch" + "\" parameterType=\"" + poClass + "\">\n");
            bw.write("\t\tINSERT INTO\n");

            StringBuffer insertFieldBuffer = new StringBuffer();
            StringBuffer insertFieldValueBuffer = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfo.getIsAutoIncrement()) {
                    continue;
                }
                insertFieldBuffer.append(fieldInfo.getFieldName()).append(",");
                insertFieldValueBuffer.append("#{item." + fieldInfo.getPropertyName() + "}").append(",");
            }
            String insertFieldBufferStr = insertFieldBuffer.substring(0, insertFieldBuffer.lastIndexOf(","));
            String insertFieldValueBufferStr = insertFieldValueBuffer.substring(0, insertFieldValueBuffer.lastIndexOf(","));

            bw.write("\t\t" + tableInfo.getTableName() + "(" + insertFieldBufferStr + ")");
            bw.newLine();
            bw.write("\t\tvalues\n");
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
            bw.write("\t\t\t(\n");
            bw.write("\t\t\t" + insertFieldValueBufferStr + "\n");
            bw.write("\t\t\t)\n");
            bw.write("\t\t</foreach>\n");
            bw.write("\t</insert>\n");
            bw.newLine();

            //批量插入或更新
            bw.write("\t<!--批量插入或更新-->\n");
            bw.write("\t<insert id=\"" + "insertOrUpdateBatch" + "\" parameterType=\"" + poClass + "\">\n");
            bw.write("\t\tINSERT INTO\n");

            bw.write("\t\t" + tableInfo.getTableName() + "(" + insertFieldBufferStr + ")");
            bw.newLine();
            bw.write("\t\tvalues\n");
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
            bw.write("\t\t\t(\n");
            bw.write("\t\t\t" + insertFieldValueBufferStr + "\n");
            bw.write("\t\t\t)\n");
            bw.write("\t\t</foreach>\n");
            bw.write("\t\tON DUPLICATE KEY UPDATE\n");

            StringBuffer updateBuffer = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (keyTempMap.get(fieldInfo.getFieldName()) != null) {
                    continue;
                }
                updateBuffer.append("\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),\n");
            }
            String updateBufferStr = updateBuffer.substring(0, updateBuffer.lastIndexOf(","));
            bw.write(updateBufferStr+"\n");
            bw.write("\t</insert>\n");
            bw.newLine();

            //根据主键、唯一索引的查询、更新、删除
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramName = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramName.append(fieldInfo.getFieldName() + "=#{"+fieldInfo.getPropertyName()+"}");
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                        paramName.append(" and ");
                    }

                }

                //根据主键、唯一索引查询
                bw.write("\t<!-- 根据" + methodName + "查询 -->\n");
                bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"" + BASE_RESULT_MAP + "\">\n");
                bw.write("\t\tselect\n");
                bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>\n");
                bw.write("\t\tfrom " + tableInfo.getTableName() + " where " + paramName + "\n");
                bw.write("\t</select>\n");
                bw.newLine();

                //根据主键、唯一索引更新
                bw.write("\t<!-- 根据" + methodName + "更新 -->\n");
                bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + poClass + "\">\n");
                bw.write("\t\tUPDATE " + tableInfo.getTableName() + "\n");
                bw.write("\t\t<set>\n");
                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                    if(fieldInfo.getIsAutoIncrement()){
                        continue;
                    }
                    bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                    bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = #{bean." + fieldInfo.getPropertyName() + "},\n");
                    bw.write("\t\t\t</if>\n");
                }
                bw.write("\t\t</set>\n");
                bw.write("\t\twhere " + paramName + "\n");
                bw.write("\t</update>\n");
                bw.newLine();

                //根据主键、唯一索引删除
                bw.write("\t<!-- 根据" + methodName + "删除 -->\n");
                bw.write("\t<delete id=\"deleteBy" + methodName + "\">\n");
                bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + paramName + "\n");
                bw.write("\t</delete>\n");
                bw.newLine();

            }

            bw.write("</mapper>");
            bw.flush();
        } catch (IOException e) {
            log.error("构建MapperXml失败", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
