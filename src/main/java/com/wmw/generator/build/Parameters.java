package com.wmw.generator.build;

import java.io.File;

/**
 * @ClassName Parameters
 * @Description TODO 参数类
 * @Author wumingwang
 * @Date 2019/5/6 9:49
 * @Version 1.0
 */
public class Parameters {

    public static String PROPERTIES_PATH = "/codegenerator/temp";

    public static String PROPERTIES = "history.propertyies";

    public static String DB_IP = "";
    public static String DB_PORT = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";
    public static String DB_DATABASE = "";

    public static String AUTHOR = "";

    public static String TEMPLATE_PATH_DEFAULT = "/template/";

    public static String TEMPLATE_PATH = "";

    public static String ENTITY_DEFAULT = "entity";
    public static String ENTITY = "";
    public static String MAPPER_CLASS_DEFAULT = "mapper_class";
    public static String MAPPER_CLASS = "";
    public static String MAPPER_XML_DEFAULT = "mapper_xml";
    public static String MAPPER_XML = "";
    public static String SERVICE_DEFAULT = "service";
    public static String SERVICE = "";
    public static String SERVICE_IMPL_DEFAULT = "service_impl";
    public static String SERVICE_IMPL = "";

    public static String PACKAGE_ENTITY = "";
    public static String PACKAGE_MAPPER_CLASS = "";
    public static String PACKAGE_MAPPER_XML = "";
    public static String PACKAGE_SERVICE = "";
    public static String PACKAGE_SERVICE_IMPL = "";

    public static boolean SERVICE_ADDTO_PACKAGE = false;

    /**
     * 下划线转驼峰命名
     */
    public static char SEPARATOR = '_';

    public static String IGNORE_STR_FIRST = "";

    /**
     * 生成代码根路径
     */
    public static String DIRECTORY = "";


}
