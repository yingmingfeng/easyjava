package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yingmingfeng
 * @classname BuildComment
 * @description 生成注解
 * @date 2026/9/21 17:04
 */
public class BuildComment {
    public static void createClassComment(BufferedWriter bw,String classComment) throws IOException{
        /**
         * @description 产品信息
         * @date 2026/9/21 17:04
         */
        bw.write("/**\n");
        bw.write(" * @author "+ Constants.COMMENT_AUTHOR+"\n");
        bw.write(" * @description "+classComment+"\n");
        bw.write(" * @date "+ DateUtils.format(new Date(),DateUtils.YYYYMMDD_HH_MM_SS) +"\n");
        bw.write(" */\n");
    }
    public static void createFieldComment(BufferedWriter bw,String fieldComment)throws IOException{
        /**
         * 商品ID
         */
        bw.write("\t/**\n");
        bw.write("\t * "+fieldComment+" "+"\n");
        bw.write("\t */\n");
    }
    public static void createMethodComment(BufferedWriter bw,String methodComment)throws IOException{
        /**
         * 商品ID
         */
        bw.write("/**\n");
        bw.write(" * "+methodComment+" "+"\n");
        bw.write(" */\n");
    }
}
