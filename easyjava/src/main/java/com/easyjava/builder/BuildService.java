package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author yingmingfeng
 * @classname BuildService
 * @description 构建Service
 * @date 2026/9/30 20:38
 */
@Slf4j
public class BuildService {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + "Service";

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, className + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            //类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"Service接口");

            bw.write("public interface " + className +  " {");
            bw.newLine();
            bw.newLine();

            //根据条件查询列表
            BuildComment.createFieldComment(bw, "根据条件查询列表");
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            //根据条件查询数量
            BuildComment.createFieldComment(bw, "根据条件查询数量");
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            //分页查询
            BuildComment.createFieldComment(bw, "根据条件查询列表,并对结果进行分页");
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            //新增
            BuildComment.createFieldComment(bw, "新增");
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();

            //批量新增
            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            //批量新增或修改
            BuildComment.createFieldComment(bw, "批量新增或修改");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            //根据主键、唯一索引的查询、更新、删除
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
                    methodParam.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if(index<keyFieldInfoList.size()){
                        methodParam.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\t"+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam +");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tInteger update"+tableInfo.getBeanName()+"By" + methodName + "(" + tableInfo.getBeanName() + " bean, "+ methodParam +");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tInteger delete"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam +");");
                bw.newLine();
                bw.newLine();

            }

            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            log.error("构建Service失败", e);
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
