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
 * @classname BuildQurey
 * @description 生成Query类
 * @date 2026/9/22 16:04
 */
@Slf4j
public class BuildQuery {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, className + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();

            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
            }

            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }

            //使用lombok注解生成setter和getter
            bw.write(Constants.BEAN_SETTER_CLASS);
            bw.newLine();
            bw.write(Constants.BEAN_GETTER_CLASS);
            bw.newLine();

            //类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());

            //使用lombok注解生成setter和getter
            bw.write(Constants.BEAN_GETTER_EXPRESSION);
            bw.newLine();
            bw.write(Constants.BEAN_SETTER_EXPRESSION);
            bw.newLine();

            bw.write("public class " + className +  " extends BaseQuery {");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();

                //String类别的参数
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    bw.newLine();
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";\n");
                    bw.newLine();
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";\n");
                    bw.newLine();
                }

            }

            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            log.error("构建Query失败", e);
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
