package com.wmw.generator.controller;

import com.wmw.generator.build.CodeGenerator;
import com.wmw.generator.build.Parameters;
import com.wmw.generator.exception.GeneratorException;
import com.wmw.generator.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.Properties;

/**
 * @ClassName MainController
 * @Description TODO
 * @Author wumingwang
 * @Date 2019/5/6 9:46
 * @Version 1.0
 */
public class MainController {

    @FXML/**自定义模板路径*/
    public TextField tempDirectory;
    @FXML/**导出模板按钮*/
    public Button tempOutBtn;
    @FXML/**浏览自定义模板按钮*/
    public Button tempInBtn;
    @FXML
    /**浏览系统目录*/
    private Button browseBtn;
    @FXML/**生成代码*/
    private Button generateBtn;
    @FXML/**代码作者*/
    private TextField author;
    @FXML/**数据库IP*/
    private TextField ip;
    @FXML/**数据库端口*/
    private TextField port;
    @FXML/**数据库名*/
    private TextField database;
    @FXML/**数据库账号*/
    private TextField user;
    @FXML/**数据库密码*/
    private TextField password;
    @FXML/**DAO层包路径*/
    private TextField packageMapperClass;
    @FXML/**XML路径*/
    private TextField packageMapperXml;
    @FXML/**SERVICE包路径*/
    private TextField packageService;
    @FXML/**serviceIMPL包路径*/
    private TextField packageServiceImpl;
    @FXML/**实体类包路径*/
    private TextField packageEntity;
    @FXML/**数据表*/
    public TextArea tables;
    @FXML/**忽略的表名前缀*/
    private TextField ignoreStrFirst;
    @FXML/**代码输出目录*/
    private TextField directory;

    public  void initViewValue(){
        Properties properties = FileUtils.readProperties();
        if(properties != null){
            author.setText(properties.getProperty("author"));
            ip.setText(properties.getProperty("ip"));
            port.setText(properties.getProperty("port"));
            database.setText(properties.getProperty("database"));
            user.setText(properties.getProperty("user"));
            password.setText(properties.getProperty("password"));
            packageMapperClass.setText(properties.getProperty("packageMapperClass"));
            packageMapperXml.setText(properties.getProperty("packageMapperXml"));
            packageService.setText(properties.getProperty("packageService"));
            packageServiceImpl.setText(properties.getProperty("packageServiceImpl"));
            packageEntity.setText(properties.getProperty("packageEntity"));
            ignoreStrFirst.setText(properties.getProperty("ignoreStrFirst"));
            directory.setText(properties.getProperty("directory"));
            tempDirectory.setText(properties.getProperty("tempDirectory"));
        }
    }
    /***
     * 初始化参数
     */
    private void initParameter(){
        /**数据库IP，非空*/
        Parameters.DB_IP = canNotBeEmpty(ip.getText(),"数据库IP不能为空！");
        /**数据库端口，非空*/
        Parameters.DB_PORT = canNotBeEmpty(port.getText(),"数据库端口不能为空！");
        /**数据库名称，非空*/
        Parameters.DB_DATABASE = canNotBeEmpty(database.getText(),"数据库名称不能为空！");
        /**数据库账户，非空*/
        Parameters.DB_USER = canNotBeEmpty(user.getText(),"数据库账户不能为空！");
        /**数据库密码，非空*/
        Parameters.DB_PASSWORD = canNotBeEmpty(password.getText(),"数据库密码不能为空！");
        /**代码作者*/
        Parameters.AUTHOR = author.getText();
        /**生成代码目录，不配置时使用程序所在目录的根目录generation文件夹下，非空*/
        String outDirectory = canNotBeEmpty(directory.getText(),"代码生成目录不能为空！");
        if(!outDirectory.endsWith(File.separator)){
            outDirectory = outDirectory + File.separator;
        }
        Parameters.DIRECTORY = outDirectory;
        /**实体包路径，非空*/
        Parameters.PACKAGE_ENTITY = canNotBeEmpty(packageEntity.getText(),"实体包路径不能为空！");
        /**DAO层包路径，非空*/
        Parameters.PACKAGE_MAPPER_CLASS = canNotBeEmpty(packageMapperClass.getText(),"DAO层包路径不能为空！");
        /**DAO对应xml路径，非空*/
        Parameters.PACKAGE_MAPPER_XML = canNotBeEmpty(packageMapperXml.getText(),"XML路径不能为空！");
        /**SERVICE层路径，非空*/
        Parameters.PACKAGE_SERVICE = canNotBeEmpty(packageService.getText(),"SERVICE包路径不能为空！");
        /**SERVICE实现层路径，非空*/
        Parameters.PACKAGE_SERVICE_IMPL = canNotBeEmpty(packageServiceImpl.getText(),"SERVICE实现层包路径不能为空！");
        /**忽略的表名前缀*/
        Parameters.IGNORE_STR_FIRST = ignoreStrFirst.getText();
        /**自定义模板路径*/
        Parameters.TEMPLATE_PATH = tempDirectory.getText();
        if(Parameters.TEMPLATE_PATH != null && Parameters.TEMPLATE_PATH.trim().length() != 0){
            if(!Parameters.TEMPLATE_PATH.endsWith(File.separator)){
                Parameters.TEMPLATE_PATH = Parameters.TEMPLATE_PATH + File.separator;
            }
            if(Parameters.ENTITY == null || Parameters.ENTITY.trim().length() == 0){
                Parameters.ENTITY = Parameters.ENTITY_DEFAULT;
            }
            if(Parameters.MAPPER_CLASS == null || Parameters.MAPPER_CLASS.trim().length() == 0){
                Parameters.MAPPER_CLASS = Parameters.MAPPER_CLASS_DEFAULT;
            }
            if(Parameters.MAPPER_XML == null || Parameters.MAPPER_XML.trim().length() == 0){
                Parameters.MAPPER_XML = Parameters.MAPPER_XML_DEFAULT;
            }
            if(Parameters.SERVICE == null || Parameters.SERVICE.trim().length() == 0){
                Parameters.SERVICE = Parameters.SERVICE_DEFAULT;
            }
            if(Parameters.SERVICE_IMPL == null || Parameters.SERVICE_IMPL.trim().length() == 0){
                Parameters.SERVICE_IMPL = Parameters.SERVICE_IMPL_DEFAULT;
            }
        }
    }

    private String canNotBeEmpty(String str,String msg){
        if(str == null || str.trim().length() == 0){
            throw new GeneratorException(msg);
        }
        return str;
    }

    @FXML
    public void browseSystem(ActionEvent event) {
        DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
        builder.title("选择代码生成路径");
        String path = System.getProperty("user.dir");
        if(directory.getText() != null && directory.getText().trim().length() != 0){
            path = directory.getText();
        }
        File file = new File(path);
        builder.initialDirectory(file);
        DirectoryChooser chooser = builder.build();
        File chosenDir = chooser.showDialog(new Stage());
        if (chosenDir != null) {
            directory.setText(chosenDir.getAbsolutePath());
        }
    }

    @FXML
    public void generateCode(ActionEvent event) {
        generateBtn.setDisable(true);
        if(tables.getText() == null || tables.getText().trim().length() == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("提示");
            alert.setContentText("当前未填写数据表，将生成"+database.getText()+"库下所有表的代码，是否继续？");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                build();
            } else {
                alert.close();
            }
        }else{
            build();
        }
        FileUtils.writeProperties();
        generateBtn.setDisable(false);
    }

    private void build(){
        try{
            initParameter();
            CodeGenerator.buildData(tables.getText());
            showMsg("成功","代码生成成功！输出目录："+directory.getText(),Alert.AlertType.INFORMATION);
        }catch (GeneratorException e){
            showMsg("错误提示",e.getMsg(),Alert.AlertType.ERROR);
        }catch (Exception e){
            showMsg("错误信息",e.getMessage(),Alert.AlertType.ERROR);
        }
    }

    private Alert showMsg(String title,String content,Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
        return alert;
    }

    /**
     * 导出默认模板
     * @param actionEvent
     */
    @FXML
    public void tempOutBrowse(ActionEvent actionEvent) {
        tempOutBtn.setDisable(true);
        DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
        builder.title("选择导出默认模板路径");
        File file = new File(System.getProperty("user.dir"));
        builder.initialDirectory(file);
        DirectoryChooser chooser = builder.build();
        File chosenDir = chooser.showDialog(new Stage());
        if (chosenDir != null) {
            String path = chosenDir.getAbsolutePath();
            Alert alert = showMsg("导出模板","导出模板到"+path+"中......",Alert.AlertType.INFORMATION);
            if(!path.endsWith(File.separator)){
                path = path + File.separator;
            }
            FileUtils.exportTempFile(path);
            alert.close();
        }
        tempOutBtn.setDisable(false);
    }

    /**
     * 使用自定义模板
     * @param actionEvent
     */
    @FXML
    public void tempInBrowse(ActionEvent actionEvent) {
        DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
        builder.title("选择自定义模板路径");
        String path = System.getProperty("user.dir");
        if(tempDirectory.getText() != null && tempDirectory.getText().trim().length() != 0){
            path = tempDirectory.getText();
        }
        File file = new File(path);
        builder.initialDirectory(file);
        DirectoryChooser chooser = builder.build();
        File chosenDir = chooser.showDialog(new Stage());
        if (chosenDir != null) {
            tempDirectory.setText(chosenDir.getAbsolutePath());
        }
    }
}
