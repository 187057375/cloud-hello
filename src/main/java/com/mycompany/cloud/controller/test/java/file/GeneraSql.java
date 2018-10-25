package com.mycompany.cloud.controller.test.java.file;


import com.mycompany.cloud.controller.test.rpc.myrpc.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GeneraSql {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    /**下划线转驼峰*/
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    /**驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})*/
    public static String humpToLine(String str){
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }
    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    /**驼峰转下划线,效率比上面高*/
    public static String humpToLine2(String str){
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    public static void main(String[] args) {


        String filePath = "/Users/baoya/landray/code/sql/sys_log_user_oper.txt";
        try {
            String encoding = "utf-8";
            StringBuffer outJava = new StringBuffer();
            StringBuffer outSQLCreateComment = new StringBuffer();
            StringBuffer outSQLCreate = new StringBuffer();
            StringBuffer outSQLInsertFd = new StringBuffer();
            StringBuffer outSQLInsertValue = new StringBuffer();
            StringBuffer outSQLInsert = new StringBuffer();
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String appName   = "DCP-SYS-LOG";
                String tableName = file.getName().substring(0,file.getName().indexOf(".")).toUpperCase();
                String javaName  =  lineToHump(tableName);
                String lineTxt = null;
                System.out.println("=================");
                System.out.println(tableName.toLowerCase());
                System.out.println("S"+javaName.substring(1));
                outSQLCreate.append("CREATE TABLE \"").append(appName).append("\".\"").append(tableName).append("\"  ").append("\n");
                outSQLCreate.append("(").append("\n");
                outSQLInsertFd.append("INSERT INTO "+tableName+"(" );
                outSQLInsertValue.append(" VALUES( ");
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if(StringUtils.isNotEmpty(lineTxt)){
                        String[] inputLine  = lineTxt.split(" ");
                        String fdComment = inputLine[0].trim();
                        String fdType = "VARCHAR2(%s)";
                        if(inputLine[1].contains("时间")){
                            fdType = "DATETIME(6)";
                        }
                        if(inputLine[1].contains("数字")){
                            fdType = "INT";
                        }
                        String  fdLen =inputLine[3].trim();
                        if(StringUtil.isEmpty(fdLen)){
                            fdLen="50";
                        }
                        if(fdType.contains("VARCHAR")){
                            fdType = String.format(fdType,fdLen);
                        }

                        String fdName=inputLine[2].trim().toUpperCase();
                        String fdPro = lineToHump(fdName);

                        //java entity 类
                        outJava.append("private String ").append(fdPro).append(";//").append(fdComment).append("\n");


                        //生成 建表sql
                        String fdIsNull = "";
                        if(fdName.equals("ID")){
                            fdIsNull = "NOT NULL";
                        }
                        String sqlFiled = String.format("   \"%s\" %s %s,\n",fdName,fdType,fdIsNull);
                        outSQLCreate.append(sqlFiled);


                        //生成 insert sql
                        outSQLInsertFd.append(String.format("%s,",fdName));
                        outSQLInsertValue.append(String.format("#{%s},",javaName+"."+fdPro));

                        outSQLCreateComment.append(String.format("COMMENT ON COLUMN \"%s\".\"%s\".\"%s\" IS '%s'; \n",appName,tableName,fdName,fdComment));

                    }
                }
                outSQLCreate.append("   CLUSTER PRIMARY KEY(\"ID\")").append("\n");
                outSQLCreate.append(")\n");
                outSQLCreate.append("STORAGE(ON \"MAIN\", CLUSTERBTR);\n");
                outSQLCreate.append(outSQLCreateComment);


                outSQLInsert.append( outSQLInsertFd.substring(0,outSQLInsertFd.length()-1)).append(")");
                outSQLInsert.append( outSQLInsertValue.substring(0,outSQLInsertValue.length()-1)).append(")");


                System.out.println(outJava.toString());
                System.out.println(outSQLCreate.toString());
                System.out.println(outSQLInsert.toString());
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }

}
