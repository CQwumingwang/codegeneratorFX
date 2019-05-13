package com.wmw.generator.utils;

import com.wmw.generator.build.Parameters;
import com.wmw.generator.exception.GeneratorException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @ClassName FileUtils
 * @Description TODO 文件工具类
 * @Author wumingwang
 * @Date 2019/5/6 9:54
 * @Version 1.0
 */
public class FileUtils {


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     * @param fileName 文件全路径名
     * @return
     */
    public static String readFileByLines(String fileName,String defaultName) {
        InputStreamReader in = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            if(fileName == null || fileName.trim().length() == 0){
                FileUtils fu = new FileUtils();
                URL location = fu.getClass().getResource(Parameters.TEMPLATE_PATH_DEFAULT+defaultName);
                in = new InputStreamReader(location.openStream(),"UTF-8");
            }else{
                File file = new File(fileName);
                in = new InputStreamReader(new FileInputStream(file),"UTF-8");
            }
            reader = new BufferedReader(in);
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString+System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneratorException(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 写文件
     * @param content 文件内容
     * @param packageStr 包路径
     * @param fileName 文件名
     */
    public static void writeFile(String content,String packageStr,String fileName){
        BufferedWriter writer = null;
        try {
            String path = getFilePath(Parameters.DIRECTORY+packageStr.replace(".",File.separator),fileName);
            writer = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (path,true), StandardCharsets.UTF_8));
            writer.write("");
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFilePath(String path,String fileName) throws IOException {
        File parten =new File(path);
        if(!parten.exists()){
            parten.mkdirs();
        }
        path = path+File.separator+fileName;
        File javaFile=new File(path);
        if(!javaFile.exists()){
            javaFile.createNewFile();
        }
        return path;
    }

    /**
     * 读取缓存文件
     * @return
     */
    public static Properties readProperties(){
        Properties properties = new Properties();
        InputStream in = null;
        try {
            File file = new File(Parameters.PROPERTIES_PATH+File.separator+Parameters.PROPERTIES);
            if(file.exists()){
                in = new BufferedInputStream (new FileInputStream(Parameters.PROPERTIES_PATH+File.separator+Parameters.PROPERTIES));
                properties.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    public static void writeProperties(){
        FileOutputStream oFile = null;
        try {
            Properties properties = new Properties();
            String path = getFilePath(Parameters.PROPERTIES_PATH,Parameters.PROPERTIES);
            oFile = new FileOutputStream(path, false);
            properties.setProperty("author", Parameters.AUTHOR);
            properties.setProperty("ip", Parameters.DB_IP);
            properties.setProperty("port", Parameters.DB_PORT);
            properties.setProperty("database", Parameters.DB_DATABASE);
            properties.setProperty("user", Parameters.DB_USER);
            properties.setProperty("password", Parameters.DB_PASSWORD);
            properties.setProperty("packageMapperClass", Parameters.PACKAGE_MAPPER_CLASS);
            properties.setProperty("packageMapperXml", Parameters.PACKAGE_MAPPER_XML);
            properties.setProperty("packageService", Parameters.PACKAGE_SERVICE);
            properties.setProperty("packageServiceImpl", Parameters.PACKAGE_SERVICE_IMPL);
            properties.setProperty("packageEntity", Parameters.PACKAGE_ENTITY);
            properties.setProperty("ignoreStrFirst", Parameters.IGNORE_STR_FIRST);
            properties.setProperty("directory", Parameters.DIRECTORY);
            properties.store(oFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(oFile != null){
                try {
                    oFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
