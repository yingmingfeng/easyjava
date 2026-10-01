package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author yingmingfeng
 * @classname BuildPo
 * @description 构建Po
 * @date 2026/9/21 15:29
 */
@Slf4j
public class BuildPo {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, tableInfo.getBeanName() + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();

            bw.write("import java.io.Serializable;");
            bw.newLine();

            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.write("import " + Constants.PACKAGE_ENUM + ".DateTimePatternEnum;\n");
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;\n");

                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS);
                bw.newLine();
            }

            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }

            //需要忽略的属性(配合lombok注解使用,使用@JsonIgnore注解会有冲突)
            Boolean haveIgnore = false;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getPropertyName())) {
                    bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS);
                    bw.newLine();
                    haveIgnore = true;
                    break;
                }
            }

            //使用lombok注解生成setter和getter
            bw.write(Constants.BEAN_SETTER_CLASS);
            bw.newLine();
            bw.write(Constants.BEAN_GETTER_CLASS);
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment());

            //使用lombok注解生成setter和getter
            bw.write(Constants.BEAN_GETTER_EXPRESSION);
            bw.newLine();
            bw.write(Constants.BEAN_SETTER_EXPRESSION);
            bw.newLine();

            //需要忽略的属性(配合lombok注解使用,使用@JsonIgnore注解会有冲突)
            if (haveIgnore) {
                // 将 逗号分隔的单词 替换为 带引号的单词
                String formattedFields = Constants.IGNORE_BEAN_TOJSON_FIELD.replaceAll("([^,]+)", "\"$1\"");
                bw.write(String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESSION, formattedFields));
                bw.newLine();
            }

            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());

                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }

                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }

            StringBuffer toString = new StringBuffer();
            //重写toString方法
            Integer index = 0;
            for (FieldInfo field : tableInfo.getFieldList()) {
                index++;

                String propertyName = field.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    propertyName = "DateUtils.format(" + propertyName + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    propertyName = "DateUtils.format(" + propertyName + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                }

                toString.append(field.getComment() + ":\" + (" + field.getPropertyName() + " == null ? \"空\" : " + propertyName + ")");
                if (index < tableInfo.getFieldList().size()) {
                    toString.append(" + ").append("\",");
                }
            }
            String toStringStr = toString.toString();
            toStringStr = "\"" + toStringStr;
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + toStringStr + ";");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            log.error("构建Po失败", e);
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
