package com.easyjava.builder;

import com.easyjava.bean.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yingmingfeng
 * @classname BuildBase
 * @description 根据template下面的txt文件生成一些项目通用的基础类文件
 * @date 2026/9/21 23:16
 */
@Slf4j
public class BuildBase {
    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        //生成Date枚举类
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "DateTimePatternEnum", Constants.PATH_ENUM);
        headerInfoList.clear();

        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList, "DateUtils", Constants.PATH_UTILS);
        headerInfoList.clear();

        //生成baseMapper接口
        headerInfoList.add("package " + Constants.PACKAGE_MAPPERS + ";");
        build(headerInfoList, "BaseMapper", Constants.PATH_MAPPERS);
        headerInfoList.clear();

        //生成PageSize枚举接口
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "PageSize", Constants.PATH_ENUM);
        headerInfoList.clear();

        //生成SimplePage query类
        //import com.easyjava.enums.PageSize;
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".PageSize;");
        build(headerInfoList, "SimplePage", Constants.PATH_QUERY);
        headerInfoList.clear();

        //生成BaseParam query类
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headerInfoList, "BaseQuery", Constants.PATH_QUERY);
        headerInfoList.clear();

        //生成PaginationResultVO类
        headerInfoList.add("package " + Constants.PACKAGE_VO + ";\n");
        headerInfoList.add("import java.util.ArrayList;");
        headerInfoList.add("import java.util.List;\n");
        build(headerInfoList, "PaginationResultVO", Constants.PATH_VO);
        headerInfoList.clear();

        //生成BusinessException类
        headerInfoList.add("package " + Constants.PACKAGE_EXCEPTION + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum;\n");
        build(headerInfoList, "BusinessException", Constants.PATH_EXCEPTION);
        headerInfoList.clear();

        //生成ResponseCodeEnum 枚举类
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "ResponseCodeEnum", Constants.PATH_ENUM);
        headerInfoList.clear();

        //生成ABaseController类
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum;\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        build(headerInfoList, "ABaseController", Constants.PATH_CONTROLLER);
        headerInfoList.clear();

        //生成ResponseVO类
        headerInfoList.add("package " + Constants.PACKAGE_VO + ";\n");
        build(headerInfoList, "ResponseVO", Constants.PATH_VO);
        headerInfoList.clear();

        //生成AGlobalExceptionHandlerController类
        headerInfoList.add("package " + Constants.PACKAGE_CONTROLLER + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + ".ResponseCodeEnum;\n");
        headerInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
        headerInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException;\n");
        headerInfoList.add("import lombok.extern.slf4j.Slf4j;\n");
        headerInfoList.add("import org.springframework.dao.DuplicateKeyException;\n");
        headerInfoList.add("import org.springframework.validation.BindException;\n");
        headerInfoList.add("import org.springframework.web.bind.annotation.ExceptionHandler;\n");
        headerInfoList.add("import org.springframework.web.bind.annotation.RestControllerAdvice;\n");
        headerInfoList.add("import org.springframework.web.servlet.NoHandlerFoundException;\n");
        headerInfoList.add("import javax.servlet.http.HttpServletRequest;\n");
        build(headerInfoList, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);
        headerInfoList.clear();

    }



    private static void build(List<String> headerInfoList,String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File javaFile = new File(outPutPath, fileName + ".java");

        // 1. 将输出流放在 try(...) 里，JVM 会自动按倒序关闭它们
        try (
                OutputStream out = new FileOutputStream(javaFile);
                OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(outw)
        ) {
            // 2. 获取模板资源，注意这里用 getResourceAsStream，而不是 getResource().getPath()
            String templatePath = "template/" + fileName + ".txt";
            InputStream in = BuildBase.class.getClassLoader().getResourceAsStream(templatePath);

            if (in == null) {
                throw new FileNotFoundException("找不到模板文件: " + templatePath);
            }

            // 3. 输入流也放在嵌套的 try(...) 里
            try (
                    InputStreamReader inr = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(inr)
            ) {
                String lineInfo;
                for(String headerInfo : headerInfoList){
                    bw.write(headerInfo);
                    bw.newLine(); // 跨平台的换行符
                }
                while ((lineInfo = br.readLine()) != null) {
                    bw.write(lineInfo);
                    bw.newLine(); // 跨平台的换行符
                }
            }

        } catch (Exception e) {
            // 统一使用日志框架，不要用 e.printStackTrace()
            log.error("生成基础类：{},失败:", fileName, e);
        }
        // 不需要写任何 finally 块！
    }
}
