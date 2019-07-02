/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordsofchaos.ui.controllers;

import com.lordsofchaos.audio.SoundAPI;
import com.lordsofchaos.ui.TeamProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author alihejazi
 */
public class SettingsController implements Initializable {

    Util utilLibrary = new Util();
    SoundAPI soundAPI = TeamProject.soundAPI;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameAudioPercentage.setText(Math.round(soundAPI.getMenuSoundVolume()) + "");
    }

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button doneButton;

    @FXML
    private Label gameAudioPercentage;


    @FXML
    private void doneButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "MainMenu.fxml");
    }


    @FXML
    void muteButtonPressed(ActionEvent event) {
        soundAPI.mute();
        gameAudioPercentage.setText( Math.round(soundAPI.getMenuSoundVolume()) + "");

    }

    @FXML
    void increaseGameAudioButtonPressed(ActionEvent event) {
        soundAPI.increaseMenuSoundVolumeBy5();
        gameAudioPercentage.setText( Math.round(soundAPI.getMenuSoundVolume()) + "");

    }

    @FXML
    void decreaseInGameAudioButtonPressed(ActionEvent event) {
        soundAPI.decreaseMenuSoundVolumeBy5();
        gameAudioPercentage.setText( Math.round(soundAPI.getMenuSoundVolume()) + "");
    }




}
