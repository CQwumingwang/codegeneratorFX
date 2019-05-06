package com.wmw.generator;

import com.wmw.generator.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @ClassName Main
 * @Description TODO 程序入口类
 * @Author wumingwang
 * @Date 2019/5/6 9:44
 * @Version 1.0
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("/fxml/code_generator.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
//        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("代码生成");
        primaryStage.setScene(new Scene(root, 608, 421));
        primaryStage.setResizable(false);
        primaryStage.show();
        MainController control = fxmlLoader.getController();
        control.initViewValue();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
