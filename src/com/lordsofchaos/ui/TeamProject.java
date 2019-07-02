package com.lordsofchaos.ui;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.lordsofchaos.audio.SoundAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;

/**
 * @author alihejazi
 */
public class TeamProject extends Application {


    public static TeamProject tp;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static SoundAPI soundAPI = new SoundAPI();

    public static void run() {
        launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/MainMenu.fxml"));

        // Create ScheduledThreadPoolExecutor Thread

        // pass in Class which constantly checks DB and updates

//        stage.setFullScreen(true);
//        stage.setResizable(false);
        float centreScreenXConstant = 1.0f / 2;
        float centreScreenYConstant = 1.0f / 3;

        Screen screen = Screen.getPrimary();
        Bounds bounds = root.getBoundsInParent();

        Rectangle2D boundsScreen = screen.getVisualBounds();

        double centerX = boundsScreen.getMinX() + (boundsScreen.getWidth() - bounds.getWidth()) * centreScreenXConstant;
        double centerY = boundsScreen.getMinY() + (boundsScreen.getHeight() - bounds.getHeight() + 29) * centreScreenYConstant;

        int initWidth = 1500;
        int initHeight = 890;

        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        //System.out.println(resolution.getWidth());
        //System.out.println(resolution.getHeight());
        //System.out.println(primaryScreenBounds.getWidth());
        //System.out.println(primaryScreenBounds.getHeight());
        double w = width/initWidth;  //your window width
        double h = height/initHeight;  //your window hight
        Scale scale = new Scale(w, h, 0, 0);
        //scale.xProperty().bind(root.widthProperty().divide(initWidth));
        //scale.yProperty().bind(root.heightProperty().divide(initHeight));
        root.getTransforms().add(scale);
        Scene scene = new Scene(root,width,height);
        tp = this;
        //stage.initStyle(StageStyle.UNDECORATED);
        //stage.setMaximized(true);
        stage.setScene(scene);
        stage.setMaximized(true);
        //stage.centerOnScreen();
        stage.show();


        // annoying

        soundAPI.playMenuSound();
    }

    public void reInitilize(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/MainMenu.fxml"));

        // Create ScheduledThreadPoolExecutor Thread

        // pass in Class which constantly checks DB and updates

//        stage.setFullScreen(true);
//        stage.setResizable(false);
        float centreScreenXConstant = 1.0f / 2;
        float centreScreenYConstant = 1.0f / 3;

        Screen screen = Screen.getPrimary();
        Bounds bounds = root.getBoundsInParent();

        Rectangle2D boundsScreen = screen.getVisualBounds();

        double centerX = boundsScreen.getMinX() + (boundsScreen.getWidth() - bounds.getWidth()) * centreScreenXConstant;
        double centerY = boundsScreen.getMinY() + (boundsScreen.getHeight() - bounds.getHeight() + 29) * centreScreenYConstant;

        int initWidth = 1500;
        int initHeight = 890;

        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        //System.out.println(resolution.getWidth());
        //System.out.println(resolution.getHeight());
        //System.out.println(primaryScreenBounds.getWidth());
        //System.out.println(primaryScreenBounds.getHeight());
        double w = width/initWidth;  //your window width
        double h = height/initHeight;  //your window hight
        Scale scale = new Scale(w, h, 0, 0);
        //scale.xProperty().bind(root.widthProperty().divide(initWidth));
        //scale.yProperty().bind(root.heightProperty().divide(initHeight));
        root.getTransforms().add(scale);
        tp = this;
        //stage.setMaximized(true);
        Scene scene = new Scene(root,width,height);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();


        // annoying

        soundAPI.playMenuSound();
    }
}