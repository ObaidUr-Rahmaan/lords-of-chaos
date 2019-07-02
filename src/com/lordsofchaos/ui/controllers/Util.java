package com.lordsofchaos.ui.controllers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lordsofchaos.audio.SoundAPI;
import com.lordsofchaos.ui.TeamProject;
import javafx.animation.RotateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * @author alihejazi
 */
public class Util {

    public void openFrame(Pane rootPane, String fxmlFrame) {

        fxmlFrame = "../views/" + fxmlFrame;

        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlFrame));
            rootPane.getChildren().setAll(pane);
            TeamProject.soundAPI.buttonClickingSound();
        } catch (Exception ex) {
            System.err.println("Error occurred. Unable to find fxml file: " + fxmlFrame);
        }

    }

}
