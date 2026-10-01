package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;
import com.easyjava.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author yingmingfeng
 * @classname BuildMapper
 * @description 构建Mapper
 * @date 2026/9/23 12:24
 */
@Slf4j
public class BuildMapper {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
            out = new FileOutputStream(new File(folder, className + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_MAPPERS + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();

            //类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + Constants.SUFFIX_MAPPERS);
            bw.write("public interface " + className + "<T, P> extends BaseMapper {");
            bw.newLine();

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()){
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParam = new StringBuilder();
                for(FieldInfo fieldInfo : keyFieldInfoList){
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if(index<keyFieldInfoList.size()){
                        methodName.append("And");
                    }
                    methodParam.append("@Param(\"" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if(index<keyFieldInfoList.size()){
                        methodParam.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\tT selectBy" + methodName + "("+ methodParam +");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, "+ methodParam +");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tInteger deleteBy" + methodName + "("+ methodParam +");");
                bw.newLine();
                bw.newLine();

            }


            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            log.error("构建Mapper失败", e);
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
