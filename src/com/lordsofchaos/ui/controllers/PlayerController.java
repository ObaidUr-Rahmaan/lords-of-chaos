package com.lordsofchaos.ui.controllers;

import com.lordsofchaos.networking.game.client.GameClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class PlayerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lobbyIPAddressLabel.setText("Lobby IP Address: " + EnterHostIPAddressController.HOST_IP);
    }

    Util utilLibary = new Util();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label lobbyIPAddressLabel;

    @FXML
    private Button readyButton;

    @FXML
    private Button leaveLobby;

    @FXML
    private ImageView loadingGif;

    @FXML
    private Label loadingMessage;

    @FXML
    void leaveLobbyButtonPressed(ActionEvent event) {
        utilLibary.openFrame(rootPane, "MainMenu.fxml");
    }

    @FXML
    void readyButtonPressed(ActionEvent event) throws Exception {

        boolean singlePlayer = false;

        loadingGif.setVisible(true);
        loadingMessage.setVisible(true);
        readyButton.setText("Ready!");
        readyButton.setDisable(true);

        System.out.println("Got here");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        GameClient joiningGameClient = new GameClient(singlePlayer, EnterHostIPAddressController.HOST_IP);
        joiningGameClient.giveStage(window);
        joiningGameClient.start(window);
    }

}

