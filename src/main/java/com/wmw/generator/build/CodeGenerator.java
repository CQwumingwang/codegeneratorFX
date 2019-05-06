package com.wmw.generator.build;

import com.wmw.generator.exception.GeneratorException;
import com.wmw.generator.utils.DBUtils;
import com.wmw.generator.utils.DateUtils;
import com.wmw.generator.utils.FileUtils;
import com.wmw.generator.utils.StringUtils;

import java.sql.ResultSet;

/**
 * @ClassName CodeGenerator
 * @Description TODO 生成代码
 * @Author wumingwang
 * @Date 2019/5/6 9:51
 * @Version 1.0
 */
public class CodeGenerator {



//    public static void main(String[] args) {
//        /**数据库IP，非空*/
//        Parameters.DB_IP = "localhost";
//        /**数据库端口，非空*/
//        Parameters.DB_PORT = "3306";
//        /**数据库名称，非空*/
//        Parameters.DB_DATABASE = "hotel";
//        /**数据库账户，非空*/
//        Parameters.DB_USER = "root";
//        /**数据库密码，非空*/
//        Parameters.DB_PASSWORD = "root";
//        /**代码作者*/
//        Parameters.AUTHOR = "wumingwang";
//        /**模板目录，不配置时使用默认模板，非空*/
//        Parameters.TEMPLATE_PATH = "D:\\IDEA_GIT\\CodeGeneration\\template\\";
//        /**生成代码目录，不配置时使用程序所在目录的根目录generation文件夹下，非空*/
//        Parameters.DIRECTORY = "D:\\data\\generation\\";
//        /**数据表名或字段分隔符，不配置时默认为下划线'_'，非空*/
//        Parameters.SEPARATOR = '_';
//        /**实体包路径，非空*/
//        Parameters.PACKAGE_ENTITY = "com.aos.sycisign.entity";
//        /**DAO层包路径，非空*/
//        Parameters.PACKAGE_MAPPER_CLASS = "com.aos.sycisign.mapper";
//        /**DAO对应xml路径，非空*/
//        Parameters.PACKAGE_MAPPER_XML = "com.aos.sycisign.mapper";
//        /**SERVICE层路径，非空*/
//        Parameters.PACKAGE_SERVICE = "com.aos.sycisign.service";
//        /**SERVICE实现层路径，非空*/
//        Parameters.PACKAGE_SERVICE_IMPL = "com.aos.sycisign.service.impl";
//        /**SERVICE层是否需要追加本身名称的包路径，如果要追加包路劲，SERVICE层和SERVICE实现层最好配置同一包路径。true:追加，false:不追加，默认不加，非空*/
//        Parameters.SERVICE_ADDTO_PACKAGE = false;
//        /**需要自动生成代码的表，为空表示生成当前库下所有表的代码*/
//        String tables = "t_web_booking,t_web_booking_file";
//        /**生成类名时忽略的表名前缀，为空表示不忽略*/
//        Parameters.IGNORE_STR_FIRST = "t_web";
//
//        /**生成代码*/
//        buildData(tables);
//
//    }

    public static void buildData(String tableNames){
        String sql = "SELECT table_name name,TABLE_COMMENT comment FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '"+Parameters.DB_DATABASE+"'";
        if(tableNames != null && !tableNames.trim().equals("")){
            sql = sql + " and table_name in (";
            StringBuilder tableSb = new StringBuilder();
            for(String tableName : tableNames.split(",")){
                tableSb.append("'").append(tableName).append("',");
            }
            sql = sql + tableSb.deleteCharAt(tableSb.length()-1) + ")";
        }

        String entity = FileUtils.readFileByLines(Parameters.TEMPLATE_PATH+Parameters.ENTITY,Parameters.ENTITY_DEFAULT);
        String mapperClass = FileUtils.readFileByLines(Parameters.TEMPLATE_PATH+Parameters.MAPPER_CLASS,Parameters.MAPPER_CLASS_DEFAULT);
        String mapperXml = FileUtils.readFileByLines(Parameters.TEMPLATE_PATH+Parameters.MAPPER_XML,Parameters.MAPPER_XML_DEFAULT);
        String service = FileUtils.readFileByLines(Parameters.TEMPLATE_PATH+Parameters.SERVICE,Parameters.SERVICE_DEFAULT);
        String serviceImpl = FileUtils.readFileByLines(Parameters.TEMPLATE_PATH+Parameters.SERVICE_IMPL,Parameters.SERVICE_IMPL_DEFAULT);

        String felids = StringUtils.interceptStr(entity,"field--","--field");

        String idNode = StringUtils.interceptStr(mapperXml,"id--","--id");
        String resultNode = StringUtils.interceptStr(mapperXml,"result--","--result");
        String valuesNode = StringUtils.interceptStr(mapperXml,"values--","--values");
        String setNode = StringUtils.interceptStr(mapperXml,"set--","--set");
        String idWhereNode = StringUtils.interceptStr(mapperXml,"idWhere--","--idWhere");
        String selectIfNode = StringUtils.interceptStr(mapperXml,"selectIf--","--selectIf");
        try {
            ResultSet rs = DBUtils.query(sql);
            while (rs != null && rs.next()) {
                String tName = rs.getString("name");
                String tcomment = rs.getString("comment");
                String csql = "select Column_Name as columnName,data_type as typeName,COLUMN_COMMENT as deText, " +
                        "(case when data_type = 'float' or data_type = 'double' or data_type = 'decimal' then NUMERIC_PRECISION else CHARACTER_MAXIMUM_LENGTH end ) as length, " +
                        "NUMERIC_SCALE as scale,( case when EXTRA='auto_increment' then 1 else 0 end) as isIdentity,(case when COLUMN_KEY='PRI' then 1 else 0 end) as isPK, " +
                        "(case when IS_NULLABLE = 'NO' then 0 else 1 end)as canNull,COLUMN_DEFAULT as defaultVal " +
                        "from information_schema.columns where table_schema = '"+Parameters.DB_DATABASE+"' and table_name = '"+tName+"'";
                String ignoreTName = tName;
                if(Parameters.IGNORE_STR_FIRST != null && Parameters.IGNORE_STR_FIRST.trim().length() != 0
                    && tName.startsWith(Parameters.IGNORE_STR_FIRST)){
                    ignoreTName = tName.replaceFirst(Parameters.IGNORE_STR_FIRST,"");
                }
                String className = StringUtils.separatorToCamel(ignoreTName);
                String classNameEntity = StringUtils.toUpperCaseFirstOne(className);
                String classNameMapper = classNameEntity + "Mapper";
                String classNameService = classNameEntity + "Service";
                String classNameServiceImpl = classNameEntity + "ServiceImpl";

                String packageService = Parameters.PACKAGE_SERVICE;
                String packageServiceImpl = Parameters.PACKAGE_SERVICE_IMPL;
                if(Parameters.SERVICE_ADDTO_PACKAGE){
                    packageService = packageService + "." + classNameService.toLowerCase();
                    packageServiceImpl = packageService + ".impl";
                }

                String entityContent = entity.replace("[author]",Parameters.AUTHOR).replace("[dateTime]", DateUtils.nowStr())
                        .replace("[package_entity]",Parameters.PACKAGE_ENTITY).replace("[classNameEntity]",classNameEntity).replace("[comment]",tcomment);

                String mapperClassContent = mapperClass.replace("[author]",Parameters.AUTHOR).replace("[dateTime]", DateUtils.nowStr())
                        .replace("[package_entity]",Parameters.PACKAGE_ENTITY).replace("[classNameEntity]",classNameEntity).replace("[comment]",tcomment)
                        .replace("[package_mapper_class]",Parameters.PACKAGE_MAPPER_CLASS).replace("[classNameMapper]",classNameMapper)
                        .replace("[classNameEntityLower]",StringUtils.toLowerCaseFirstOne(classNameEntity));

                String mapperXmlContent = mapperXml.replace("[package_mapper_class]",Parameters.PACKAGE_MAPPER_CLASS).replace("[classNameMapper]",classNameMapper)
                        .replace("[package_entity]",Parameters.PACKAGE_ENTITY).replace("[classNameEntity]",classNameEntity)
                        .replace("[tableName]",tName);

                String serviceContent = service.replace("[author]",Parameters.AUTHOR).replace("[dateTime]", DateUtils.nowStr())
                        .replace("[package_entity]",Parameters.PACKAGE_ENTITY).replace("[classNameEntity]",classNameEntity).replace("[comment]",tcomment)
                        .replace("[package_service]",packageService).replace("[classNameService]",classNameService)
                        .replace("[classNameEntityLower]",StringUtils.toLowerCaseFirstOne(classNameEntity));

                String serviceImplContent = serviceImpl.replace("[author]",Parameters.AUTHOR).replace("[dateTime]", DateUtils.nowStr())
                        .replace("[package_entity]",Parameters.PACKAGE_ENTITY).replace("[classNameEntity]",classNameEntity).replace("[comment]",tcomment)
                        .replace("[package_service]",packageService).replace("[classNameService]",classNameService)
                        .replace("[package_service_impl]",packageServiceImpl).replace("[classNameServiceImpl]",classNameServiceImpl)
                        .replace("[package_mapper_class]",Parameters.PACKAGE_MAPPER_CLASS).replace("[classNameMapper]",classNameMapper)
                        .replace("[classNameEntityLower]",StringUtils.toLowerCaseFirstOne(classNameEntity))
                        .replace("[classNameMapperLower]",StringUtils.toLowerCaseFirstOne(classNameMapper));

                ResultSet rsc = DBUtils.query(csql);
                StringBuilder fieldsSb = new StringBuilder();
                StringBuilder importEntitySb = new StringBuilder();

                StringBuilder idSb = new StringBuilder();
                StringBuilder resultSb = new StringBuilder();
                StringBuilder valuesSb = new StringBuilder();
                StringBuilder setSb = new StringBuilder();
                StringBuilder selectIfSb = new StringBuilder();
                StringBuilder columns = new StringBuilder();
                String idCName= null;
                String idTypeName= null;
                while (rsc != null && rsc.next()) {
                    String cName = rsc.getString("columnName");
                    String typeName = rsc.getString("typeName");
                    String deText = rsc.getString("deText");
                    columns.append(cName).append(",");
                    if(rsc.getInt("isPK") == 1){
                        idCName = cName;
                        idTypeName = typeName;
                    }
                    if(idCName == null){
                        idCName = cName;
                        idTypeName = typeName;
                    }
                    String property = StringUtils.separatorToCamel(cName);
                    String fs = felids.replace("[deText]",deText).replace("[cName]",property)
                            .replace("[typeName]",FieldComparison.getField(typeName));
                    fieldsSb.append(fs);
                    String imp = FieldComparison.getImport(typeName);
                    if(!imp.equals("") && importEntitySb.indexOf(imp) < 0){
                        importEntitySb.append(imp).append(";").append(System.getProperty("line.separator"));
                    }
                    String jdbcType = FieldComparison.getJdbcType(typeName);
                    if(rsc.getInt("isPK") == 1){
                        String ids = idNode.replace("[column]",cName).replace("[property]",property)
                                .replace("[jdbcType]",jdbcType);
                        idSb.append(ids);

                    }else{
                        String results = resultNode.replace("[column]",cName).replace("[property]",property)
                                .replace("[jdbcType]",jdbcType);
                        resultSb.append(results);
                    }
                    String valuess = valuesNode.replace("[property]",property).replace("[jdbcType]",jdbcType);
                    valuesSb.append(valuess).append(",");
                    if(rsc.getInt("isPK") == 0){
                        String sets = setNode.replace("[column]",cName).replace("[property]",property).replace("[jdbcType]",jdbcType);
                        setSb.append(sets).append(",");
                    }
                    String selectIfs = selectIfNode.replace("[column]",cName).replace("[property]",property).replace("[jdbcType]",jdbcType);
                    if(FieldComparison.getField(typeName).equals("String")){
                        selectIfs = selectIfs.replace("[strEmpty]"," and "+property+" != ''");
                    }else{
                        selectIfs = selectIfs.replace("[strEmpty]","");
                    }
                    selectIfSb.append(selectIfs);

                }
                String idProperty = StringUtils.separatorToCamel(idCName);
                String idJdbcType = FieldComparison.getJdbcType(idTypeName);
                String idImport = FieldComparison.getImport(idTypeName);
                if(!idImport.equals("")){
                    idImport = idImport + ";";
                }

                entityContent = entityContent.replace("[import]",importEntitySb).replace("field--"+felids+"--field",fieldsSb);
                FileUtils.writeFile(entityContent,Parameters.PACKAGE_ENTITY,classNameEntity+".java");

                mapperClassContent = mapperClassContent.replace("[idCName]",idProperty)
                        .replace("[idTypeName]",FieldComparison.getField(idTypeName)).replace("[import]",idImport);
                FileUtils.writeFile(mapperClassContent,Parameters.PACKAGE_MAPPER_CLASS,classNameMapper+".java");

                String idParameterType = FieldComparison.getImport(idTypeName);
                if(idParameterType.equals("")){
                    idParameterType = "java.lang."+ FieldComparison.getField(idTypeName);
                }
                columns.deleteCharAt(columns.length()-1);
                valuesSb.deleteCharAt(valuesSb.length()-1);
                setSb.deleteCharAt(setSb.length()-1);
                String idWhere = idWhereNode.replace("[idColumn]",idCName).replace("[idProperty]",idProperty).replace("[idJdbcType]",idJdbcType);
                mapperXmlContent = mapperXmlContent.replace("[idParameterType]",idParameterType).replace("[columns]",columns)
                        .replace("id--"+idNode+"--id",idSb).replace("result--"+resultNode+"--result",resultSb)
                        .replace("values--"+valuesNode+"--values",valuesSb).replace("set--"+setNode+"--set",setSb)
                        .replace("idWhere--"+idWhereNode+"--idWhere",idWhere).replace("[idWhere]",idWhere)
                        .replace("selectIf--"+selectIfNode+"--selectIf",selectIfSb).replace("[selectIf]",selectIfSb)
                        .replace("[idColumn]",idCName).replace("[idJdbcType]",idJdbcType);
                FileUtils.writeFile(mapperXmlContent,Parameters.PACKAGE_MAPPER_XML,classNameMapper+".xml");

                serviceContent = serviceContent.replace("[idCName]",idProperty)
                        .replace("[idTypeName]",FieldComparison.getField(idTypeName)).replace("[import]",idImport);
                FileUtils.writeFile(serviceContent,packageService,classNameService+".java");

                serviceImplContent = serviceImplContent.replace("[idCName]",idProperty)
                        .replace("[idTypeName]",FieldComparison.getField(idTypeName)).replace("[import]",idImport)
                        .replace("[idCNameUpper]",StringUtils.toUpperCaseFirstOne(idProperty));
                FileUtils.writeFile(serviceImplContent,packageServiceImpl,classNameServiceImpl+".java");
                rsc.close();
            }
            rs.close();
            DBUtils.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new GeneratorException(e.getMessage());
        }

    }

}
