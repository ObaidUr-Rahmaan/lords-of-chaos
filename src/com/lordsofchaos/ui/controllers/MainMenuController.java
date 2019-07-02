package com.lordsofchaos.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {

    Util utilLibrary = new Util();


    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button joinLobbyButton;

    @FXML
    void createLobbyButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "PlayerHost.fxml");

        // Create variables for Lobby name and password

        // Prompt User for Lobby name and password

        // Set variables to inputted name and password

        // Create Lobby with those variables + THIS User's IP as parameters (i.e. Constants.getGameServerIPAddress)

        // Connect to DB Server ----------- IP Address: 178.128.35.154

        // Specify

        // Close DB Server Connection -----------
    }

    @FXML
    void joinLobbyButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "EnterHostIPAddress.fxml");

        // Prompt User for Lobby to join (name and password)

        // Once User types in values:

        // Connect to DB Server ----------- IP Address: 178.128.35.154

        // Specify and then execute a Prepared Statement SQL Query which checks if the Lobby exists in the 'Lobbies' table

        // If Exists:
        // Update table by incrementing clientsInLobby by 1

        // Else:
        // Show on screen in the prompt that the Lobby does not exist and that they should try again

        // Close DB Server Connection -----------

    }

    @FXML
    void exitButtonPressed(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void howToPlayButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "HowToPlay.fxml");
    }

    @FXML
    void settingsButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "Settings.fxml");
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

}
