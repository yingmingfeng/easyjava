package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;

/**
 * @author yingmingfeng
 * @classname Constants
 * @description 常量
 * @date 2026/9/20 19:44
 */
public class Constants {

    //是否忽略表前嘴 tb_product_info => product_info
    public static Boolean IGNORE_TABLE_PREFIX ;

    //参数bean后缀
    public static String SUFFIX_BEAN_QUERY ;

    //参数模糊搜索后缀
    public static String SUFFIX_BEAN_QUERY_FUZZY ;

    //参数日期起止
    public static String SUFFIX_BEAN_QUERY_TIME_START ;
    public static String SUFFIX_BEAN_QUERY_TIME_END ;

    //mapper的后缀
    public static String SUFFIX_MAPPERS ;

    //需要忽略的属性(配合lombok注解使用,使用@JsonIgnore注解会有冲突)
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;

    //日期格式序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;

    //日期格式反序列化
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    //使用lombok注解生成setter和getter
    public static String BEAN_SETTER_EXPRESSION;
    public static String BEAN_SETTER_CLASS;
    public static String BEAN_GETTER_EXPRESSION;
    public static String BEAN_GETTER_CLASS;

    //包名
    public static String PATH_BASE ;
    public static String PATH_PO ;
    public static String PATH_VO ;
    public static String PATH_QUERY ;
    public static String PATH_UTILS ;
    public static String PATH_ENUM ;
    public static String PATH_EXCEPTION ;

    public static String PATH_MAPPERS ;
    public static String PATH_MAPPERS_XMLS ;
    public static String PATH_SERVICE ;
    public static String PATH_SERVICE_IMPL ;
    public static String PATH_CONTROLLER ;

    public static String PACKAGE_BASE ;
    public static String PACKAGE_PO ;
    public static String PACKAGE_VO ;
    public static String PACKAGE_QUERY ;
    public static String PACKAGE_UTILS ;
    public static String PACKAGE_ENUM ;
    public static String PACKAGE_EXCEPTION ;
    public static String PACKAGE_MAPPERS ;
    public static String PACKAGE_SERVICE ;
    public static String PACKAGE_SERVICE_IMPL ;
    public static String PACKAGE_CONTROLLER ;

    //作者名称
    public static String COMMENT_AUTHOR ;

    private static String PACKAGE_JAVA = "java";
    private static String PACKAGE_RESOURCES = "resources";


    static {
        //是否忽略表前嘴 tb_product_info => product_info
        IGNORE_TABLE_PREFIX = Boolean.parseBoolean(PropertiesUtils.getString("ignore.table.prefix"));

        //参数bean后缀
        SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");

        //参数模糊搜索后缀
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");

        //参数日期起止
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");

        //mapper的后缀
        SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");

        //需要忽略的属性(配合lombok注解使用,使用@JsonIgnore注解会有冲突)
        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");

        //日期格式序列化
        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");

        //日期格式反序列化
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unformat.class");

        //使用lombok注解生成setter和getter
        BEAN_SETTER_EXPRESSION = PropertiesUtils.getString("bean.setter.expression");
        BEAN_SETTER_CLASS = PropertiesUtils.getString("bean.setter.class");

        BEAN_GETTER_EXPRESSION = PropertiesUtils.getString("bean.getter.expression");
        BEAN_GETTER_CLASS = PropertiesUtils.getString("bean.getter.class");

        //包名
        PATH_BASE = PropertiesUtils.getString("path.base");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUM = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");

        PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");

        //包路径
        PATH_BASE = PATH_BASE  + PACKAGE_JAVA ;
        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/") ;
        PATH_VO = PATH_BASE + "/" + PACKAGE_VO.replace(".", "/") ;

        PATH_QUERY = PATH_BASE + "/" + PACKAGE_QUERY.replace(".", "/") ;
        PATH_UTILS = PATH_BASE + "/" + PACKAGE_UTILS.replace(".", "/") ;
        PATH_ENUM = PATH_BASE + "/" + PACKAGE_ENUM.replace(".", "/") ;
        PATH_EXCEPTION = PATH_BASE + "/" + PACKAGE_EXCEPTION.replace(".", "/") ;

        PATH_MAPPERS = PATH_BASE + "/" + PACKAGE_MAPPERS.replace(".", "/") ;
        PATH_MAPPERS_XMLS = PropertiesUtils.getString("path.base")  + PACKAGE_RESOURCES+"/" +PACKAGE_MAPPERS.replace(".", "/");
        PATH_SERVICE = PATH_BASE + "/" + PACKAGE_SERVICE.replace(".", "/") ;
        PATH_SERVICE_IMPL = PATH_BASE + "/" + PACKAGE_SERVICE_IMPL.replace(".", "/") ;
        PATH_CONTROLLER = PATH_BASE + "/" + PACKAGE_CONTROLLER.replace(".", "/") ;

        //作者名称
        COMMENT_AUTHOR = PropertiesUtils.getString("comment.author");
    }
    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};

    public final static String[] SQL_DATE_TYPES = new String[]{"date"};

    public static final String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};

    public static final String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};

    //Integer
    public static final String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};

    //Long
    public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println("PATH_BASE:" + PATH_BASE);
        System.out.println("PATH_PO:" + PATH_PO);
        System.out.println("PATH_QUERY:" + PATH_QUERY);
        System.out.println("PATH_UTILS:" + PATH_UTILS);
        System.out.println("PATH_ENUM:" + PATH_ENUM);
        System.out.println("PATH_MAPPERS:" + PATH_MAPPERS);
        System.out.println("PATH_MAPPERS_XMLS:" + PATH_MAPPERS_XMLS);

        System.out.println("\n");
        System.out.println("PACKAGE_PO:" + PACKAGE_PO);
        System.out.println("PACKAGE_QUERY:" + PACKAGE_QUERY);
        System.out.println("PACKAGE_UTILS:" + PACKAGE_UTILS);
        System.out.println("PACKAGE_ENUM:" + PACKAGE_ENUM);
        System.out.println("PACKAGE_MAPPERS:" + PACKAGE_MAPPERS);

    }
}
