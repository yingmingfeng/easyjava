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
 * @classname BuildServiceImpl
 * @description 生成ServiceImpl
 * @date 2026/9/30 22:34
 */
@Slf4j
public class BuildServiceImpl {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + "ServiceImpl";
        String interfaceName = tableInfo.getBeanName() + "Service";
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        String mapperBeanName = StringUtils.lowerCaseFirstLetter(mapperName);

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(new File(folder, className + ".java"));
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+".SimplePage;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_ENUM+".PageSize;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_MAPPERS+"."+mapperName+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_SERVICE+"."+interfaceName+";");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;\n");
            bw.newLine();
            bw.write("import javax.annotation.Resource;\n");
            bw.newLine();

            //类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"ServiceImpl实现类");
            bw.write("@Service(\""+StringUtils.lowerCaseFirstLetter(interfaceName)+"\")\n");
            bw.write("public class " + className +  " implements "+interfaceName+" {");
            bw.newLine();
            bw.newLine();
            bw.write("\t@Resource\n");
            bw.write("\tprivate "+mapperName+"<"+tableInfo.getBeanName()+","+tableInfo.getBeanParamName()+"> "+mapperBeanName+";\n");
            bw.newLine();

            //根据条件查询列表
            BuildComment.createFieldComment(bw, "根据条件查询列表");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query){\n");
            bw.write("\t\treturn this."+mapperBeanName+".selectList(query);\n");
            bw.write("\t}\n");
            bw.newLine();

            //根据条件查询数量
            BuildComment.createFieldComment(bw, "根据条件查询数量");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " query){\n");
            bw.write("\t\treturn this."+mapperBeanName+".selectCount(query);\n");
            bw.write("\t}\n");
            bw.newLine();

            //分页查询
            BuildComment.createFieldComment(bw, "根据条件查询列表,并对结果进行分页");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query){\n");
            bw.write("\t\tInteger count = this.findCountByParam(query);\n");
            bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();\n");
            bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(), count, pageSize);\n");
            bw.write("\t\tquery.setSimplePage(page);\n");
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);\n");
            bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName() + "> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);\n");
            bw.write("\t\treturn result;\n");
            bw.write("\t}\n");
            bw.newLine();

            //新增
            BuildComment.createFieldComment(bw, "新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean){\n");
            bw.write("\t\treturn this."+mapperBeanName+".insert(bean);\n");
            bw.write("\t}\n");
            bw.newLine();

            //批量新增
            BuildComment.createFieldComment(bw, "批量新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean){\n");
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {\n");
            bw.write("\t\t\treturn 0;\n");
            bw.write("\t\t}\n");
            bw.write("\t\treturn this."+mapperBeanName+".insertBatch(listBean);\n");
            bw.write("\t}\n");
            bw.newLine();

            //批量新增或修改
            BuildComment.createFieldComment(bw, "批量新增或修改");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean){\n");
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {\n");
            bw.write("\t\t\treturn 0;\n");
            bw.write("\t\t}\n");
            bw.write("\t\treturn this."+mapperBeanName+".insertOrUpdateBatch(listBean);\n");
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
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic "+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam +"){\n");
                bw.write("\t\treturn this."+mapperBeanName+".selectBy"+methodName+"("+paramBuilder+");\n");
                bw.write("\t}\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic Integer update"+tableInfo.getBeanName()+"By" + methodName + "(" + tableInfo.getBeanName() + " bean, "+ methodParam +"){\n");
                bw.write("\t\treturn this."+mapperBeanName+".updateBy"+methodName+"(bean, "+paramBuilder+");\n");
                bw.write("\t}\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\t@Override");
                bw.newLine();
                bw.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By" + methodName + "("+ methodParam + "){\n");
                bw.write("\t\treturn this."+mapperBeanName+".deleteBy"+methodName+"("+paramBuilder+");\n");
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
