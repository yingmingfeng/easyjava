package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author yingmingfeng
 * @classname BuildController
 * @description 生成Controller类
 * @date 2026/10/1 18:40
 */
@Slf4j
public class BuildController {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + "Controller";
        String serviceName = tableInfo.getBeanName() + "Service";
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, className + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".ResponseVO;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_SERVICE+"."+serviceName+";");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;\n");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;\n");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bw.newLine();
            bw.write("import javax.annotation.Resource;\n");
            bw.newLine();

            //类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"Controller实现类");
            bw.write("@RestController\n");
            bw.write("@RequestMapping(\"/"+StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName())+"\")\n");
            bw.write("public class " + className +" extends ABaseController {");
            bw.newLine();
            bw.newLine();
            bw.write("\t@Resource\n");
            bw.write("\tprivate "+serviceName+" "+serviceBeanName+";\n");
            bw.newLine();

            //根据条件分页查询
            BuildComment.createFieldComment(bw, "根据条件分页查询");
            bw.write("\t@RequestMapping(\"loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParamName() + " query){\n");
            bw.write("\t\treturn this.getSuccessResponseVO("+serviceBeanName+".findListByPage(query));\n");
            bw.write("\t}\n");
            bw.newLine();

            //新增
            BuildComment.createFieldComment(bw, "新增");
            bw.write("\t@RequestMapping(\"add\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean){\n");
            bw.write("\t\t"+serviceBeanName+".add(bean);\n");
            bw.write("\t\treturn this.getSuccessResponseVO(null);\n");
            bw.write("\t}\n");
            bw.newLine();

            //批量新增
            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("\t@RequestMapping(\"addBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean){\n");
            bw.write("\t\t"+serviceBeanName+".addBatch(listBean);\n");
            bw.write("\t\treturn this.getSuccessResponseVO(null);\n");
            bw.write("\t}\n");
            bw.newLine();

            //批量新增或修改
            BuildComment.createFieldComment(bw, "批量新增或修改");
            bw.write("\t@RequestMapping(\"addOrUpdateBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean){\n");
            bw.write("\t\t"+serviceBeanName+".addOrUpdateBatch(listBean);\n");
            bw.write("\t\treturn this.getSuccessResponseVO(null);\n");
            bw.write("\t}\n");
            bw.newLine();

            //根据主键、唯一索引的查询、更新、删除
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()){
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParam = new StringBuilder();
                StringBuilder paramBuilder = new StringBuilder();
                for(FieldInfo fieldInfo : keyFieldInfoList){
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if(index<keyFieldInfoList.size()){
                        methodName.append("And");
                    }
                    methodParam.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    paramBuilder.append(fieldInfo.getPropertyName());
                    if(index<keyFieldInfoList.size()){
                        methodParam.append(", ");
                        paramBuilder.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");

                bw.write("\t@RequestMapping(\"get"+tableInfo.getBeanName()+"By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO get"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam +"){\n");
                bw.write("\t\treturn this.getSuccessResponseVO("+serviceBeanName+".get"+tableInfo.getBeanName()+"By"+methodName+"("+paramBuilder+"));\n");
                bw.write("\t}\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\t@RequestMapping(\"update"+tableInfo.getBeanName()+"By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO update"+tableInfo.getBeanName()+"By" + methodName + "(" + tableInfo.getBeanName() + " bean, "+ methodParam +"){\n");
                bw.write("\t\t"+serviceBeanName+".update"+tableInfo.getBeanName()+"By"+methodName+"(bean, "+paramBuilder+");\n");
                bw.write("\t\treturn this.getSuccessResponseVO(null);\n");
                bw.write("\t}\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\t@RequestMapping(\"delete"+tableInfo.getBeanName()+"By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO delete"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam + "){\n");
                bw.write("\t\t"+serviceBeanName+".delete"+tableInfo.getBeanName()+"By"+methodName+"("+paramBuilder+");\n");
                bw.write("\t\treturn this.getSuccessResponseVO(null);\n");
                bw.write("\t}\n");
                bw.newLine();

            }

            bw.write("}");
            bw.flush();
        } catch (IOException e) {
            log.error("构建ServiceImpl失败", e);
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
