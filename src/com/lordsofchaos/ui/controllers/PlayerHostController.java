package com.lordsofchaos.ui.controllers;


import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.networking.game.client.GameClient;
import com.lordsofchaos.networking.game.server.GameServer;
import com.lordsofchaos.networking.global.Constants;
import com.lordsofchaos.networking.shared.Lobby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;


public class PlayerHostController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        readyButton.setDisable(true);
        lobbyIPAddressLabel.setText("Lobby IP Address: " + Constants.getGameServerIPAddress());
    }

    Util utilLibrary = new Util();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label lobbyIPAddressLabel;

    @FXML
    private Label startingGameLabel;

    @FXML
    public Button leaveLobby;

    @FXML
    public Button singlePlayer;

    @FXML
    public Button multiPlayer;

    @FXML
    private Button readyButton;

    @FXML
    private CheckBox sandDustCheckBox;

    @FXML
    private CheckBox greenFarmCheckBox;

    @FXML
    void leaveLobbyButtonPressed(ActionEvent event) {
        utilLibrary.openFrame(rootPane, "MainMenu.fxml");
    }

    @FXML
    void greenFarmChecked(ActionEvent event) {
        sandDustCheckBox.setSelected(false);
    }

    @FXML
    void sandDustChecked(ActionEvent event) {
        greenFarmCheckBox.setSelected(false);
    }

    @FXML
    void singlePlayer(ActionEvent event) throws Exception {

//        startingGameLabel.setText("Loading game...");

        System.out.println(InetAddress.getLocalHost());

        Lobby freshLobby = new Lobby("only-one-rip", "only-one-rip", InetAddress.getByName(Constants.SINGLE_PLAYER_SERVER_IP));
        GameWorld freshGameWorld = new GameWorld(1, 3, sandDustCheckBox.isSelected());

        // Start GameServer
        Thread freshGameServer = new Thread(new GameServer(freshLobby, freshGameWorld));
        freshGameServer.start();

        runClient(event, true);
    }

    @FXML
    void startMultiPlayerServer(ActionEvent event) throws Exception {

        Lobby freshLobby = new Lobby("only-one-rip", "only-one-rip", Constants.getGameServerIPAddress());
        GameWorld freshGameWorld = new GameWorld(4, 0, sandDustCheckBox.isSelected());

        // Start GameServer
        Thread freshGameServer = new Thread(new GameServer(freshLobby, freshGameWorld));
        freshGameServer.start();

        startingGameLabel.setText("Waiting for Clients to connect...");

        readyButton.setDisable(false);
        multiPlayer.setDisable(true);
        singlePlayer.setDisable(true);
    }

    @FXML
    void readyButtonPressed(ActionEvent event) throws Exception {
        runClient(event, false);
        return;
    }

    private void runClient(ActionEvent event, boolean singlePlayer) throws Exception {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GameClient gameClient = new GameClient(singlePlayer);
        gameClient.giveStage(window);
        gameClient.start(window);
    }

}
